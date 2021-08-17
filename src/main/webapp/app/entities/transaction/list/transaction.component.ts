import { Authority } from './../../../config/authority.constants';
import { AccountService } from './../../../core/auth/account.service';
import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbDate, NgbCalendar, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { ITransaction } from '../transaction.model';
import { TransactionService } from '../service/transaction.service';
import { TransactionDeleteDialogComponent } from '../delete/transaction-delete-dialog.component';
import { FormGroup, FormControl } from '@angular/forms';
import { IICon } from '../../../shared/icon-picker/icon.model';
import { IconService } from '../../../shared/icon-picker/service/icon.service';
import { TransactionState } from '../../enumerations/transaction-state.model';

@Component({
  selector: 'jhi-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css'],
})
export class TransactionComponent implements OnInit {
  transactions?: ITransaction[];
  allTransactions?: ITransaction[];
  isLoading = false;
  inputText = '';
  slctDataType: string;
  selcetedValue: string;
  adminUser = false;
  fileName = '';
  page = 1;
  pageSize = 5;

  range = new FormGroup({
    start: new FormControl(),
    end: new FormControl(),
  });

  constructor(
    protected http: HttpClient,
    protected accountService: AccountService,
    protected transactionService: TransactionService,
    protected modalService: NgbModal,
    protected iconService: IconService
  ) {}

  loadAll(): void {
    /* eslint-disable no-console */
    this.isLoading = true;
    this.transactionService.query().subscribe(
      (res: HttpResponse<ITransaction[]>) => {
        this.isLoading = false;
        this.transactions = res.body ?? [];
        this.allTransactions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.isAdmin();
    this.loadAll();
  }

  onFileSelected(event) {
    const file: File = event.target.files[0];

    if (file) {
      this.fileName = file.name;

      const formData = new FormData();

      formData.append('file', file);

      const upload$ = this.http.post('/api/invoicexml/upload', formData);

      upload$.subscribe(() => {
        this.loadAll();
      });
    }
  }

  isAdmin(): void {
    this.adminUser = this.accountService.hasAnyAuthority(Authority.ADMIN);
  }

  filterTransactions(): void {
    if (this.transactions !== undefined) {
      this.transactions = this.transactions.filter(transaction => {
        if (transaction.name !== undefined) {
          return transaction.name.toLowerCase().includes(this.inputText.toLowerCase());
        } else {
          return false;
        }
      });
      if (this.inputText === '') {
        this.transactions = this.allTransactions;
      }
    } else {
      this.loadAll();
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

  trackId(index: number, item: ITransaction): number {
    return item.id!;
  }
  getIcon(iconId: number): IICon {
    return this.iconService.getIcon(iconId);
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
