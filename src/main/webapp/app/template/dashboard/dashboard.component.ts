import { Component, OnInit } from '@angular/core';
import { WalletBalanceService } from '../../reports/wallet-balance/service/wallet-balance.service';
import { HttpResponse } from '@angular/common/http';
import { IWalletBalance } from '../../reports/wallet-balance/wallet-balance.model';
import { ITotalBalance } from '../../reports/total-balance/total-balance.model';
import { TotalBalanceService } from '../../reports/total-balance/Service/total-balance.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
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
  }

  quickTransfer = [
    {
      name: 'David',
      username: '@davidxc',
      image: 'assets/images/avatar/1.jpg',
      url: 'admin/transactions-details',
    },
    {
      name: 'Cindy',
      username: '@cindyss',
      image: 'assets/images/avatar/2.jpg',
      url: 'admin/transactions-details',
    },
    {
      name: 'Samuel',
      username: '@sam224',
      image: 'assets/images/avatar/3.jpg',
      url: 'admin/transactions-details',
    },
    {
      name: 'Olivia',
      username: '@oliv62',
      image: 'assets/images/avatar/4.jpg',
      url: 'admin/transactions-details',
    },
    {
      name: 'Martha',
      username: '@marthaa',
      image: 'assets/images/avatar/5.jpg',
      url: 'admin/transactions-details',
    },
  ];
}
