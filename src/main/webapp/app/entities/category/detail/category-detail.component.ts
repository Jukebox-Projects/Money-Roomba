import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICategory } from '../category.model';
import { Authority } from '../../../config/authority.constants';
import { AccountService } from '../../../core/auth/account.service';

@Component({
  selector: 'jhi-category-detail',
  templateUrl: './category-detail.component.html',
})
export class CategoryDetailComponent implements OnInit {
  category: ICategory | null = null;
  adminUser = false;

  constructor(protected activatedRoute: ActivatedRoute, protected accountService: AccountService) {}

  ngOnInit(): void {
    this.isAdmin();
    this.activatedRoute.data.subscribe(({ category }) => {
      this.category = category;
    });
  }

  previousState(): void {
    window.history.back();
  }

  isAdmin(): void {
    this.adminUser = this.accountService.hasAnyAuthority(Authority.ADMIN);
  }
}
