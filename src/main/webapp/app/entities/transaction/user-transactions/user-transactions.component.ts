import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IconService } from '../../../shared/icon-picker/service/icon.service';
import { IWallet } from '../../wallet/wallet.model';
import { IICon } from '../../../shared/icon-picker/icon.model';
import { ITransaction } from '../transaction.model';
import { TransactionService } from '../service/transaction.service';
import { HttpResponse } from '@angular/common/http';
import { TransactionState } from '../../enumerations/transaction-state.model';
import { TransactionDeleteDialogComponent } from '../delete/transaction-delete-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IScheduledTransaction } from '../../scheduled-transaction/scheduled-transaction.model';
import { ScheduledTransactionService } from '../../scheduled-transaction/service/scheduled-transaction.service';
@Component({
  selector: 'app-user-transactions',
  templateUrl: './user-transactions.component.html',
  styleUrls: ['./user-transactions.component.css'],
})
export class UserTransactionsComponent implements OnInit {
  wallet: IWallet | null = null;
  transactions?: ITransaction[];
  allTransactions?: ITransaction[];
  scheduledTransactions?: IScheduledTransaction[];
  allScheduledTransactions?: IScheduledTransaction[];
  isLoading = false;
  collectionSize: any;
  @Input()
  walletID: number;

  constructor(
    protected transactionService: TransactionService,
    protected scheduledTransactionService: ScheduledTransactionService,
    protected activatedRoute: ActivatedRoute,
    protected iconService: IconService,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    /* eslint-disable no-console */
    this.isLoading = true;
    this.transactionService.getAllByWallet(this.walletID).subscribe(
      (res: HttpResponse<ITransaction[]>) => {
        this.isLoading = false;
        this.transactions = res.body ?? [];
        this.allTransactions = res.body ?? [];
        this.collectionSize = this.transactions.length;
      },
      () => {
        this.isLoading = false;
      }
    );
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

  trackId(index: number, item: ITransaction): number {
    return item.id!;
  }
  trackIdScheduled(index: number, item: IScheduledTransaction): number {
    return item.id!;
  }

  getIcon(iconId: number): IICon {
    return this.iconService.getIcon(iconId);
  }

  delete(transaction: ITransaction): void {
    const modalRef = this.modalService.open(TransactionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.transaction = transaction;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
  isPending(transactionState: TransactionState): boolean {
    if (transactionState === null) {
      return false;
    }
    if (transactionState.toString() === 'PENDING_APPROVAL') {
      return true;
    } else {
      return false;
    }
  }
}
