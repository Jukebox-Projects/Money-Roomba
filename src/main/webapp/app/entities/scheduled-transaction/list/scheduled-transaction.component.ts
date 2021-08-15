import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { IScheduledTransaction } from '../scheduled-transaction.model';
import { ScheduledTransactionService } from '../service/scheduled-transaction.service';
import { ScheduledTransactionDeleteDialogComponent } from '../delete/scheduled-transaction-delete-dialog.component';
import { IICon } from '../../../shared/icon-picker/icon.model';
import { IconService } from '../../../shared/icon-picker/service/icon.service';
import { AccountService } from 'app/core/auth/account.service';
import { Authority } from 'app/config/authority.constants';

@Component({
  selector: 'jhi-scheduled-transaction',
  templateUrl: './scheduled-transaction.component.html',
  styleUrls: ['./scheduled-transaction.component.css'],
})
export class ScheduledTransactionComponent implements OnInit {
  scheduledTransactions?: IScheduledTransaction[];
  allTransactions?: IScheduledTransaction[];
  isLoading = false;
  inputText = '';
  filterType: string = 'name';
  adminUser: boolean = false;

  constructor(
    protected scheduledTransactionService: ScheduledTransactionService,
    protected modalService: NgbModal,
    protected iconService: IconService,
    protected accountService: AccountService
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
    this.isAdmin();
  }

  isAdmin(): void {
    this.adminUser = this.accountService.hasAnyAuthority(Authority.ADMIN);
  }

  trackId(index: number, item: IScheduledTransaction): number {
    return item.id!;
  }
  filterTransactionsByName(): void {
    /* eslint-disable no-console */
    if (this.scheduledTransactions !== undefined) {
      this.scheduledTransactions = this.scheduledTransactions.filter(t => {
        if (t.name !== undefined) {
          return t.name.toLowerCase().includes(this.inputText.toLowerCase());
        } else {
          return false;
        }
      });
    }
    if (this.inputText === '') {
      this.scheduledTransactions = this.allTransactions;
    }
  }

  filterTransactionsByCurrency(): void {
    /* eslint-disable no-console */
    if (this.scheduledTransactions !== undefined) {
      this.scheduledTransactions = this.scheduledTransactions.filter(t => {
        if (t.currency.name !== undefined) {
          return (
            t.currency.name.toLowerCase().includes(this.inputText.toLowerCase()) ||
            t.currency.code.toLowerCase().includes(this.inputText.toLowerCase())
          );
        } else {
          return false;
        }
      });
    }
    if (this.inputText === '') {
      this.scheduledTransactions = this.allTransactions;
    }
  }

  getIcon(iconId: number): IICon {
    return this.iconService.getIcon(iconId);
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
