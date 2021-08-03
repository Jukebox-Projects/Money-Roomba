import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategory, Category } from '../category.model';
import { IICon } from '../../../shared/icon-picker/icon.model';
import { CategoryService, EntityArrayResponseType } from '../service/category.service';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
import { IconPickerComponent } from '../../../shared/icon-picker/icon-picker.component';
import { IconService } from '../../../shared/icon-picker/service/icon.service';

@Component({
  selector: 'jhi-category-update',
  templateUrl: './category-update.component.html',
  styleUrls: ['./category-update.component.css'],
})
export class CategoryUpdateComponent implements OnInit {
  isSaving = false;

  categoriesSharedCollection: ICategory[] = [];
  userDetailsSharedCollection: IUserDetails[] = [];
  selectedIcon: IICon;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    icon: [null, [Validators.required]],
    parent: [],
  });

  constructor(
    protected categoryService: CategoryService,
    protected userDetailsService: UserDetailsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected iconService: IconService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ category }) => {
      this.updateForm(category);

      this.loadRelationshipsOptions(category);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const category = this.createFromForm();
    if (category.id !== undefined) {
      this.subscribeToSaveResponse(this.categoryService.update(category));
    } else {
      this.subscribeToSaveResponse(this.categoryService.create(category));
    }
  }

  trackCategoryById(index: number, item: ICategory): number {
    return item.id!;
  }

  trackUserDetailsById(index: number, item: IUserDetails): number {
    return item.id!;
  }

  openIconPickerModal(): void {
    const modalRef = this.modalService.open(IconPickerComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.icon = this.selectedIcon;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(icon => {
      this.selectedIcon = icon;
      this.editForm.get(['icon']).patchValue(icon.id);
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(category: ICategory): void {
    this.editForm.patchValue({
      id: category.id,
      name: category.name,
      isActive: category.isActive,
      userCreated: category.userCreated,
      icon: category.icon,
      parent: category.parent,
      user: category.user,
    });

    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(
      this.categoriesSharedCollection,
      category.parent
    );
    this.userDetailsSharedCollection = this.userDetailsService.addUserDetailsToCollectionIfMissing(
      this.userDetailsSharedCollection,
      category.user
    );

    this.selectedIcon = this.iconService.getIcon(category.icon);
  }

  protected loadRelationshipsOptions(category?: ICategory): void {
    // Category dropdown only shows parent categories (child categories cannot have childs) and does not show the same category

    this.categoryService
      .query(category?.id ? { 'parentId.specified': false, 'id.notEquals': category.id } : { 'parentId.specified': false })
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, this.editForm.get('parent')!.value)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));
  }

  protected createFromForm(): ICategory {
    return {
      ...new Category(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      isActive: true,
      icon: this.editForm.get(['icon'])!.value,
      parent: this.editForm.get(['parent'])!.value,
    };
  }
}
