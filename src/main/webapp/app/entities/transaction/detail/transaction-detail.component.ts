import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransaction } from '../transaction.model';
import { IICon } from '../../../shared/icon-picker/icon.model';
import { IconService } from '../../../shared/icon-picker/service/icon.service';

@Component({
  selector: 'jhi-transaction-detail',
  templateUrl: './transaction-detail.component.html',
})
export class TransactionDetailComponent implements OnInit {
  transaction: ITransaction | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected iconService: IconService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.transaction = transaction;
    });
  }

  previousState(): void {
    window.history.back();
  }
  getIcon(iconId: number): IICon {
    return this.iconService.getIcon(iconId);
  }
}
