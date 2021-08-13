import { IconService } from './../../../shared/icon-picker/service/icon.service';
import { IICon } from './../../../shared/icon-picker/icon.model';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IScheduledTransaction } from '../scheduled-transaction.model';
import { RecurringType } from 'app/entities/enumerations/recurring-type.model';

@Component({
  selector: 'jhi-scheduled-transaction-detail',
  templateUrl: './scheduled-transaction-detail.component.html',
})
export class ScheduledTransactionDetailComponent implements OnInit {
  scheduledTransaction: IScheduledTransaction | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected iconService: IconService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scheduledTransaction }) => {
      this.scheduledTransaction = scheduledTransaction;
    });
  }

  previousState(): void {
    window.history.back();
  }
  getIcon(iconId: number): IICon {
    return this.iconService.getIcon(iconId);
  }

  isDaily(): Boolean {
    return this.scheduledTransaction.recurringType == RecurringType.DAILY;
  }

  isWeekly(): Boolean {
    return this.scheduledTransaction.recurringType == RecurringType.WEEKLY;
  }

  isMonthly(): Boolean {
    return this.scheduledTransaction.recurringType == RecurringType.MONTHLY;
  }

  isYearly(): Boolean {
    return this.scheduledTransaction.recurringType == RecurringType.YEARLY;
  }
}
