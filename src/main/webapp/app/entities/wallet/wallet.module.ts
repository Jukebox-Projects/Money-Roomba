import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WalletComponent } from './list/wallet.component';
import { WalletDetailComponent } from './detail/wallet-detail.component';
import { WalletUpdateComponent } from './update/wallet-update.component';
import { WalletDeleteDialogComponent } from './delete/wallet-delete-dialog.component';
import { WalletRoutingModule } from './route/wallet-routing.module';
import { IconService } from '../../shared/icon-picker/service/icon.service';
import { WalletSliderComponent } from './wallet-center/wallet-slider/wallet-slider.component';
import { WalletStatisticComponent } from './wallet-center/wallet-statistic/wallet-statistic.component';
import { CarouselModule } from 'ngx-owl-carousel-o';

@NgModule({
  imports: [SharedModule, WalletRoutingModule, CarouselModule],
  declarations: [
    WalletComponent,
    WalletDetailComponent,
    WalletUpdateComponent,
    WalletDeleteDialogComponent,
    WalletSliderComponent,
    WalletStatisticComponent,
  ],
  entryComponents: [WalletDeleteDialogComponent],
  providers: [IconService],
})
export class WalletModule {}
