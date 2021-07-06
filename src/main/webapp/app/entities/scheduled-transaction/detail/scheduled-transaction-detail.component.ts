import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IScheduledTransaction } from '../scheduled-transaction.model';

@Component({
  selector: 'jhi-scheduled-transaction-detail',
  templateUrl: './scheduled-transaction-detail.component.html',
})
export class ScheduledTransactionDetailComponent implements OnInit {
  scheduledTransaction: IScheduledTransaction | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scheduledTransaction }) => {
      this.scheduledTransaction = scheduledTransaction;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
