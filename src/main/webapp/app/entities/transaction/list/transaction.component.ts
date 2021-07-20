import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITransaction } from '../transaction.model';
import { TransactionService } from '../service/transaction.service';
import { TransactionDeleteDialogComponent } from '../delete/transaction-delete-dialog.component';
import { IWallet } from '../../wallet/wallet.model';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'jhi-transaction',
  templateUrl: './transaction.component.html',
})
export class TransactionComponent implements OnInit {
  transactions?: ITransaction[];
  allTransactions?: IWallet[];
  isLoading = false;
  inputText = '';
  slctDataType: string;

  range = new FormGroup({
    start: new FormControl(),
    end: new FormControl(),
  });

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
    if (this.transactions !== undefined) {
      this.transactions = this.transactions.filter(transaction => {
        if (transaction.name !== undefined || transaction.description !== undefined) {
          return (
            transaction.name.toLowerCase().includes(this.inputText.toLowerCase()) ||
            transaction.description.toLowerCase().includes(this.inputText.toLowerCase())
          );
        } else {
          return false;
        }
      });
      if (this.inputText === '') {
        this.transactions = this.allTransactions;
      }
    }
  }

  filterDateRange(): void {
    /* eslint-disable no-console */
    if (this.transactions !== undefined) {
      this.transactions = this.transactions.filter(transaction => {
        if (transaction.dateAdded !== undefined) {
          return (
            transaction.dateAdded.toDate().valueOf() >= this.range.value.start.valueOf() &&
            transaction.dateAdded.toDate().valueOf() <= this.range.value.end.valueOf()
          );
        }
      });
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
