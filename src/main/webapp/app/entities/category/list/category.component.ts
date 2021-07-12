import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AccountService } from '../../../core/auth/account.service';

import { ICategory } from '../category.model';
import { CategoryService } from '../service/category.service';
import { CategoryDeleteDialogComponent } from '../delete/category-delete-dialog.component';
import { CategoryStatusDialogComponent } from '../status/category-status-dialog.component';
import { Authority } from '../../../config/authority.constants';

@Component({
  selector: 'jhi-category',
  templateUrl: './category.component.html',
})
export class CategoryComponent implements OnInit {
  categories?: ICategory[];
  allCategories?: ICategory[];
  isLoading = false;
  inputText = '';
  adminUser = false;

  constructor(protected categoryService: CategoryService, protected modalService: NgbModal, protected accountService: AccountService) {}

  loadAll(): void {
    this.isLoading = true;

    this.categoryService.query().subscribe(
      (res: HttpResponse<ICategory[]>) => {
        this.isLoading = false;
        this.categories = res.body ?? [];
        this.allCategories = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.isAdmin();
    this.loadAll();
  }

  trackId(index: number, item: ICategory): number {
    return item.id!;
  }

  filterCategory(): void {
    if (this.categories !== undefined) {
      this.categories = this.categories.filter(category => {
        if (category.name !== undefined) {
          return category.name.toLowerCase().includes(this.inputText.toLowerCase());
        } else {
          return false;
        }
      });
      if (this.inputText === '') {
        this.categories = this.allCategories;
      }
    }
  }

  delete(category: ICategory): void {
    const modalRef = this.modalService.open(CategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.category = category;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  status(category: ICategory): void {
    const modalRef = this.modalService.open(CategoryStatusDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.category = category;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'status changed') {
        this.loadAll();
      }
    });
  }

  isAdmin(): void {
    this.adminUser = this.accountService.hasAnyAuthority(Authority.ADMIN);
  }
}
