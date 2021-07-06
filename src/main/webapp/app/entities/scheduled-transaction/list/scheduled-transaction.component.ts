import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IScheduledTransaction } from '../scheduled-transaction.model';
import { ScheduledTransactionService } from '../service/scheduled-transaction.service';
import { ScheduledTransactionDeleteDialogComponent } from '../delete/scheduled-transaction-delete-dialog.component';

@Component({
  selector: 'jhi-scheduled-transaction',
  templateUrl: './scheduled-transaction.component.html',
})
export class ScheduledTransactionComponent implements OnInit {
  scheduledTransactions?: IScheduledTransaction[];
  isLoading = false;

  constructor(protected scheduledTransactionService: ScheduledTransactionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.scheduledTransactionService.query().subscribe(
      (res: HttpResponse<IScheduledTransaction[]>) => {
        this.isLoading = false;
        this.scheduledTransactions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IScheduledTransaction): number {
    return item.id!;
  }

  delete(scheduledTransaction: IScheduledTransaction): void {
    const modalRef = this.modalService.open(ScheduledTransactionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.scheduledTransaction = scheduledTransaction;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
