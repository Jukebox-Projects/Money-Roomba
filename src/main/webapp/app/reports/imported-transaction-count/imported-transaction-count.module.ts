import { NgApexchartsModule } from 'ng-apexcharts';
import { ImportedTransactionCountComponent } from './imported-transaction-count.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [ImportedTransactionCountComponent],
  imports: [CommonModule, SharedModule, NgApexchartsModule],
  exports: [ImportedTransactionCountComponent],
})
export class ImportedTransactionCountModule {}
