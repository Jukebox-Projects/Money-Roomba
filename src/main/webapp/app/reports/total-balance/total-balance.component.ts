import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ITotalBalance } from './total-balance.model';
import { TotalBalanceService } from './Service/total-balance.service';
import { WalletBalanceService } from '../wallet-balance/service/wallet-balance.service';
import { MovementType } from '../../entities/enumerations/movement-type.model';

@Component({
  selector: 'jhi-total-balance',
  templateUrl: './total-balance.component.html',
  styleUrls: ['./total-balance.component.scss'],
})
export class TotalBalanceComponent implements OnInit {
  reportData: ITotalBalance[];
  totalBalance: number;
  data: ITotalBalance[];
  constructor(protected totalBalanceService: TotalBalanceService) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    /* eslint-disable no-console */
    this.totalBalanceService.queryAll().subscribe(
      (res: HttpResponse<ITotalBalance[]>) => {
        this.reportData = res.body ?? [];
        this.resolveData(this.reportData);
      },
      error => {}
    );
  }

  getSymbol() {
    return this.data && this.data.length > 0 ? this.data[0]?.currency?.code || '' : '';
  }

  protected resolveData(reportData: ITotalBalance[]): void {
    /* eslint-disable no-console */
    this.totalBalance = 0;
    this.data = this.reportData;
    for (let i in reportData) {
      this.totalBalance = this.totalBalance + reportData[i].total;
    }
  }
}
