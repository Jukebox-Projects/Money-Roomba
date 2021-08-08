import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ITotalBalance } from './total-balance.model';
import { TotalBalanceService } from './Service/total-balance.service';
import { WalletBalanceService } from '../wallet-balance/service/wallet-balance.service';

@Component({
  selector: 'jhi-total-balance',
  templateUrl: './total-balance.component.html',
  styleUrls: ['./total-balance.component.scss'],
})
export class TotalBalanceComponent implements OnInit {
  reportData: ITotalBalance[];
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

  protected resolveData(reportData: ITotalBalance[]): void {
    /* eslint-disable no-console */
    console.log('WEBOS');
    console.log(this.reportData);
    console.log('WEBOS');
  }
}
