import { Component, OnInit } from '@angular/core';
import { WalletBalanceService } from '../../reports/wallet-balance/service/wallet-balance.service';
import { HttpResponse } from '@angular/common/http';
import { IWalletBalance } from '../../reports/wallet-balance/wallet-balance.model';
import { ITotalBalance } from '../../reports/total-balance/total-balance.model';
import { TotalBalanceService } from '../../reports/total-balance/Service/total-balance.service';
import { faCaretDown } from '@fortawesome/free-solid-svg-icons';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { faCalendarAlt } from '@fortawesome/free-solid-svg-icons';
import { Authority } from '../../config/authority.constants';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  reportData: ITotalBalance[];
  faCaretDown = faCaretDown;
  faCalendarAlt = faCalendarAlt;
  account: Account | null = null;
  showDateInputs: boolean = false;
  adminUser = false;
  constructor(protected totalBalanceService: TotalBalanceService, private accountService: AccountService) {}

  ngOnInit(): void {
    this.loadAll();
    this.accountService.getAuthenticationState().subscribe(async account => (this.account = account));
    this.isAdmin();
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
  onClick() {
    this.showDateInputs = true;
  }

  isAdmin(): void {
    this.adminUser = this.accountService.hasAnyAuthority(Authority.ADMIN);
  }
}
