import { NgModule } from '@angular/core';
import { TransactionCountComponent } from './transaction-count.component';
import { CommonModule } from '@angular/common';
import { DatePipe } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [TransactionCountComponent],
  imports: [CommonModule, SharedModule],
  exports: [TransactionCountComponent],
  providers: [DatePipe],
})
export class TransactionCountModule {}
