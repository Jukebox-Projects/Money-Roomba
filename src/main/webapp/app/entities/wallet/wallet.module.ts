import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WalletComponent } from './list/wallet.component';
import { WalletDetailComponent } from './detail/wallet-detail.component';
import { WalletUpdateComponent } from './update/wallet-update.component';
import { WalletDeleteDialogComponent } from './delete/wallet-delete-dialog.component';
import { WalletRoutingModule } from './route/wallet-routing.module';
import { IconService } from '../../shared/icon-picker/service/icon.service';
import { UserTransactionsComponent } from '../transaction/user-transactions/user-transactions.component';
import { UserScheduledTransactionsComponent } from '../scheduled-transaction/user-scheduled-transactions/user-scheduled-transactions.component';
import { WalletSliderComponent } from './wallet-center/wallet-slider/wallet-slider.component';
import { WalletStatisticComponent } from './wallet-center/wallet-statistic/wallet-statistic.component';
import { CarouselModule } from 'ngx-owl-carousel-o';
import { NgApexchartsModule } from 'ng-apexcharts';
import { WalletBalanceModule } from '../../reports/wallet-balance/wallet-balance.module';
export * from '../scheduled-transaction/user-scheduled-transactions/user-scheduled-transactions.component';
export * from '../transaction/user-transactions/user-transactions.component';

@NgModule({
  imports: [SharedModule, WalletRoutingModule, CarouselModule, NgApexchartsModule, WalletBalanceModule],
  declarations: [
    WalletComponent,
    WalletDetailComponent,
    WalletUpdateComponent,
    WalletDeleteDialogComponent,
    UserTransactionsComponent,
    UserScheduledTransactionsComponent,
    WalletSliderComponent,
    WalletStatisticComponent,
  ],
  entryComponents: [WalletDeleteDialogComponent],
  providers: [IconService],
})
export class WalletModule {}
