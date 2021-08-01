import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ScheduledTransactionService } from '../service/scheduled-transaction.service';
import { IconService } from '../../../shared/icon-picker/service/icon.service';
import { IICon } from '../../../shared/icon-picker/icon.model';
import { HttpResponse } from '@angular/common/http';
import { TransactionState } from '../../enumerations/transaction-state.model';
import { IScheduledTransaction } from '../scheduled-transaction.model';
import { TransactionDeleteDialogComponent } from '../../transaction/delete/transaction-delete-dialog.component';

@Component({
  selector: 'app-user-scheduled-transactions',
  templateUrl: './user-scheduled-transactions.component.html',
  styleUrls: ['./user-scheduled-transactions.component.scss'],
})
export class UserScheduledTransactionsComponent implements OnInit {
  scheduledTransactions?: IScheduledTransaction[];
  allTransactions?: IScheduledTransaction[];
  isLoading = false;
  collectionSize: any;

  constructor(
    protected scheduledTransactionService: ScheduledTransactionService,
    protected activatedRoute: ActivatedRoute,
    protected iconService: IconService,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;
    this.scheduledTransactionService.query().subscribe(
      (res: HttpResponse<IScheduledTransaction[]>) => {
        this.isLoading = false;
        this.scheduledTransactions = res.body ?? [];
        this.allTransactions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
  ngOnInit(): void {
    this.loadAll();
  }

  previousState(): void {
    window.history.back();
  }

  trackId(index: number, item: IScheduledTransaction): number {
    return item.id!;
  }

  getIcon(iconId: number): IICon {
    return this.iconService.getIcon(iconId);
  }

  delete(scheduledTransaction: IScheduledTransaction): void {
    const modalRef = this.modalService.open(TransactionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.scheduledTransaction = scheduledTransaction;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
