import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { IWalletBalance } from './wallet-balance.model';
import { WalletBalanceService } from './service/wallet-balance.service';

@Component({
  selector: 'jhi-wallet-balance',
  templateUrl: './wallet-balance.component.html',
  styleUrls: ['./wallet-balance.component.scss'],
})
export class WalletBalanceComponent implements OnInit {
  reportData: IWalletBalance;
  result = '';

  constructor(protected walletBalanceService: WalletBalanceService) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.walletBalanceService.query().subscribe(
      (res: HttpResponse<IWalletBalance>) => {
        this.reportData = res.body ?? null;
      },
      error => {
        this.reportData = error.message;
      }
    );
  }
}
