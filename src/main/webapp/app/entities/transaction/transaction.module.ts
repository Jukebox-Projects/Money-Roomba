import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TransactionComponent } from './list/transaction.component';
import { TransactionDetailComponent } from './detail/transaction-detail.component';
import { TransactionUpdateComponent } from './update/transaction-update.component';
import { TransactionDeleteDialogComponent } from './delete/transaction-delete-dialog.component';
import { TransactionRoutingModule } from './route/transaction-routing.module';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { IconService } from '../../shared/icon-picker/service/icon.service';
export * from '../scheduled-transaction/user-scheduled-transactions/user-scheduled-transactions.component';
@NgModule({
  imports: [SharedModule, TransactionRoutingModule, MatFormFieldModule, MatDatepickerModule],
  declarations: [TransactionComponent, TransactionDetailComponent, TransactionUpdateComponent, TransactionDeleteDialogComponent],
  entryComponents: [TransactionDeleteDialogComponent],
  providers: [IconService],
})
export class TransactionModule {}
