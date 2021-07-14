import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICategory, Category } from '../category.model';
import { CategoryService, EntityArrayResponseType } from '../service/category.service';
import { IIcon } from 'app/entities/icon/icon.model';
import { IconService } from 'app/entities/icon/service/icon.service';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';

@Component({
  selector: 'jhi-category-update',
  templateUrl: './category-update.component.html',
})
export class CategoryUpdateComponent implements OnInit {
  isSaving = false;

  categoriesSharedCollection: ICategory[] = [];
  iconsSharedCollection: IIcon[] = [];
  userDetailsSharedCollection: IUserDetails[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    icon: [],
    parent: [],
  });

  constructor(
    protected categoryService: CategoryService,
    protected iconService: IconService,
    protected userDetailsService: UserDetailsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
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

  trackIconById(index: number, item: IIcon): number {
    return item.id!;
  }

  trackUserDetailsById(index: number, item: IUserDetails): number {
    return item.id!;
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
    this.iconsSharedCollection = this.iconService.addIconToCollectionIfMissing(this.iconsSharedCollection, category.icon);
    this.userDetailsSharedCollection = this.userDetailsService.addUserDetailsToCollectionIfMissing(
      this.userDetailsSharedCollection,
      category.user
    );
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

    this.iconService
      .query()
      .pipe(map((res: HttpResponse<IIcon[]>) => res.body ?? []))
      .pipe(map((icons: IIcon[]) => this.iconService.addIconToCollectionIfMissing(icons, this.editForm.get('icon')!.value)))
      .subscribe((icons: IIcon[]) => (this.iconsSharedCollection = icons));
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
