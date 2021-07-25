import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AccountService } from '../../../core/auth/account.service';
import { IWallet } from '../wallet.model';
import { WalletService } from '../service/wallet.service';
import { WalletDeleteDialogComponent } from '../delete/wallet-delete-dialog.component';
import { Authority } from '../../../config/authority.constants';
import { IICon } from '../../../shared/icon-picker/icon.model';
import { IconService } from '../../../shared/icon-picker/service/icon.service';

@Component({
  selector: 'jhi-wallet',
  templateUrl: './wallet.component.html',
  styleUrls: ['./wallet.component.css'],
})
export class WalletComponent implements OnInit {
  wallets?: IWallet[];
  allwallets?: IWallet[];
  isLoading = false;
  inputText = '';
  adminUser = false;

  constructor(
    protected walletService: WalletService,
    protected accountService: AccountService,
    protected modalService: NgbModal,
    protected iconService: IconService
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.walletService.query().subscribe(
      (res: HttpResponse<IWallet[]>) => {
        this.isLoading = false;
        this.wallets = res.body ?? [];
        this.allwallets = res.body ?? [];
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

  trackId(index: number, item: IWallet): number {
    return item.id!;
  }

  filterWallets(): void {
    /* eslint-disable no-console */
    if (this.wallets !== undefined) {
      this.wallets = this.wallets.filter(wallet => {
        if (wallet.name !== undefined) {
          return wallet.name.toLowerCase().includes(this.inputText.toLowerCase());
        } else {
          return false;
        }
      });
    }
    if (this.inputText === '') {
      this.wallets = this.allwallets;
    }
  }

  delete(wallet: IWallet): void {
    const modalRef = this.modalService.open(WalletDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.wallet = wallet;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  isAdmin(): void {
    this.adminUser = this.accountService.hasAnyAuthority(Authority.ADMIN);
  }

  getIcon(iconId: number): IICon {
    return this.iconService.getIcon(iconId);
  }
}
