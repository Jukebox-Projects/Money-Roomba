import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WalletComponent } from './list/wallet.component';
import { WalletDetailComponent } from './detail/wallet-detail.component';
import { WalletUpdateComponent } from './update/wallet-update.component';
import { WalletDeleteDialogComponent } from './delete/wallet-delete-dialog.component';
import { WalletRoutingModule } from './route/wallet-routing.module';
import { IconService } from '../../shared/icon-picker/service/icon.service';
import { UserTransactionsComponent } from '../transaction/user-transactions/user-transactions.component';
export * from '../transaction/user-transactions/user-transactions.component';
@NgModule({
  imports: [SharedModule, WalletRoutingModule],
  declarations: [WalletComponent, WalletDetailComponent, WalletUpdateComponent, WalletDeleteDialogComponent, UserTransactionsComponent],
  entryComponents: [WalletDeleteDialogComponent],
  providers: [IconService],
})
export class WalletModule {}
