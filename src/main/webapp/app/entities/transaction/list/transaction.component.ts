import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITransaction } from '../transaction.model';
import { TransactionService } from '../service/transaction.service';
import { TransactionDeleteDialogComponent } from '../delete/transaction-delete-dialog.component';
import { IWallet } from '../../wallet/wallet.model';

@Component({
  selector: 'jhi-transaction',
  templateUrl: './transaction.component.html',
})
export class TransactionComponent implements OnInit {
  transactions?: ITransaction[];
  allTransactions?: IWallet[];
  isLoading = false;
  inputText = '';
  selected = '';

  constructor(protected transactionService: TransactionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.transactionService.query().subscribe(
      (res: HttpResponse<ITransaction[]>) => {
        this.isLoading = false;
        this.transactions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITransaction): number {
    return item.id!;
  }

  filterTransactions(): void {
    /* eslint-disable no-console */
    if (this.transactions !== undefined) {
      this.transactions = this.transactions.filter(transaction => {
        if (transaction.name !== undefined && this.selected === 'name1') {
          return transaction.name.toLowerCase().includes(this.inputText.toLowerCase());
        } else if (transaction.description !== undefined && this.selected === 'description1') {
          return transaction.description.toLowerCase().includes(this.inputText.toLowerCase());
        } else {
          return false;
        }
      });
    }
    if (this.inputText === '') {
      this.transactions = this.allTransactions;
    }
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
}
