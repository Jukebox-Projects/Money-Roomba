import { NgModule } from '@angular/core';
import { WalletBalanceComponent } from './wallet-balance.component';
import { CommonModule } from '@angular/common';
import { DatePipe } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [WalletBalanceComponent],
  imports: [CommonModule, SharedModule],
  exports: [WalletBalanceComponent],
  providers: [DatePipe],
})
export class WalletBalanceModule {}
