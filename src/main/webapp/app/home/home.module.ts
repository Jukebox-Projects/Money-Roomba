import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { DASHBOARD_ROUTE, HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { DashboardComponent } from '../template/dashboard/dashboard.component';
import { GraphIncomeComponent } from '../template/elements/dashboard/graph-income/graph-income.component';

import { NgApexchartsModule } from 'ng-apexcharts';
import { GraphOutomeComponent } from '../template/elements/dashboard/graph-outome/graph-outome.component';
import { GraphLimitComponent } from '../template/elements/dashboard/graph-limit/graph-limit.component';
import { GraphTransactionOverviewComponent } from '../template/elements/dashboard/graph-transaction-overview/graph-transaction-overview.component';
import { GraphWeeklyWalletUsageComponent } from '../template/elements/dashboard/graph-weekly-wallet-usage/graph-weekly-wallet-usage.component';
import { SpendingsComponent } from '../template/elements/dashboard/spendings/spendings.component';
import { InstallmentComponent } from '../template/elements/dashboard/spendings/installment/installment.component';
import { InvestmentComponent } from '../template/elements/dashboard/spendings/investment/investment.component';
import { PropertyComponent } from '../template/elements/dashboard/spendings/property/property.component';
import { RestaurantComponent } from '../template/elements/dashboard/spendings/restaurant/restaurant.component';
import { WalletBalanceModule } from '../reports/wallet-balance/wallet-balance.module';
import { TransactionCountModule } from '../reports/transaction-count/transaction-count.module';

@NgModule({
  imports: [
    SharedModule,
    NgApexchartsModule,
    RouterModule.forChild([HOME_ROUTE, DASHBOARD_ROUTE]),
    WalletBalanceModule,
    TransactionCountModule,
  ],
  declarations: [
    HomeComponent,
    DashboardComponent,
    GraphIncomeComponent,
    GraphOutomeComponent,
    GraphLimitComponent,
    GraphTransactionOverviewComponent,
    GraphWeeklyWalletUsageComponent,
    SpendingsComponent, //Begin spendings components
    InstallmentComponent,
    InvestmentComponent,
    PropertyComponent,
    RestaurantComponent, // End spendings components
  ],
})
export class HomeModule {}
