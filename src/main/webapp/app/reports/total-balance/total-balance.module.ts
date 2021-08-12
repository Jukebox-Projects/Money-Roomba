import { NgModule } from '@angular/core';
import { TotalBalanceComponent } from './total-balance.component';
import { CommonModule } from '@angular/common';
import { DatePipe } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [TotalBalanceComponent],
  imports: [CommonModule, SharedModule, RouterModule],
  exports: [TotalBalanceComponent],
  providers: [DatePipe],
})
export class TotalBalanceModule {}
