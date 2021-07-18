import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICurrency } from '../currency.model';
import { Authority } from '../../../config/authority.constants';
import { AccountService } from '../../../core/auth/account.service';

@Component({
  selector: 'jhi-currency-detail',
  templateUrl: './currency-detail.component.html',
})
export class CurrencyDetailComponent implements OnInit {
  currency: ICurrency | null = null;
  adminUser = false;

  constructor(protected activatedRoute: ActivatedRoute, protected accountService: AccountService) {}

  ngOnInit(): void {
    this.isAdmin();
    this.activatedRoute.data.subscribe(({ currency }) => {
      this.currency = currency;
    });
  }

  previousState(): void {
    window.history.back();
  }

  isAdmin(): void {
    this.adminUser = this.accountService.hasAnyAuthority(Authority.ADMIN);
  }
}
