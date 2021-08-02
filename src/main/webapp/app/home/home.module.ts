import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { DASHBOARD_ROUTE, HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { WalletBalanceComponent } from '../reports/wallet-balance/wallet-balance.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE, DASHBOARD_ROUTE])],
  declarations: [HomeComponent, WalletBalanceComponent],
})
export class HomeModule {}
