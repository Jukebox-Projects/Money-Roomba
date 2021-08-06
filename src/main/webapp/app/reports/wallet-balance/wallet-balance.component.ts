import { Component, Input, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { IWalletBalance } from './wallet-balance.model';
import { WalletBalanceService } from './service/wallet-balance.service';
import { MovementType } from '../../entities/enumerations/movement-type.model';
import { Wallet } from '../../entities/wallet/wallet.model';

@Component({
  selector: 'jhi-wallet-balance',
  templateUrl: './wallet-balance.component.html',
  styleUrls: ['./wallet-balance.component.scss'],
})
export class WalletBalanceComponent implements OnInit {
  reportData: IWalletBalance[];
  income: IWalletBalance;
  expense: IWalletBalance;
  @Input() wallet: Wallet | null;

  constructor(protected walletBalanceService: WalletBalanceService) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    if (this.wallet) {
      this.walletBalanceService.query(this.wallet.id).subscribe(
        (res: HttpResponse<IWalletBalance[]>) => {
          this.reportData = res.body ?? [];
          this.resolveData(this.reportData);
        },
        error => {}
      );
    } else {
      this.walletBalanceService.queryAll().subscribe(
        (res: HttpResponse<IWalletBalance[]>) => {
          this.reportData = res.body ?? [];
          this.resolveData(this.reportData);
        },
        error => {}
      );
    }
  }

  protected resolveData(reportData: IWalletBalance[]): void {
    if (reportData.length == 2) {
      if (reportData[0].movementType == MovementType.INCOME && reportData[1].movementType == MovementType.EXPENSE) {
        this.income = reportData[0];
        this.expense = reportData[1];
      } else if (reportData[1].movementType == MovementType.INCOME && reportData[0].movementType == MovementType.EXPENSE) {
        this.income = reportData[1];
        this.expense = reportData[0];
      }
    } else if (reportData.length == 1) {
      if (reportData[0].movementType == MovementType.INCOME) {
        this.income = reportData[0];
      } else {
        this.expense = reportData[0];
      }
    } else {
      this.income = { total: 0, movementType: MovementType.INCOME, currency: { code: '' } };
      this.expense = { total: 0, movementType: MovementType.EXPENSE, currency: { code: '' } };
    }
  }
}
