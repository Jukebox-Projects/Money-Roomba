import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ScheduledTransactionComponent } from './list/scheduled-transaction.component';
import { ScheduledTransactionDetailComponent } from './detail/scheduled-transaction-detail.component';
import { ScheduledTransactionUpdateComponent } from './update/scheduled-transaction-update.component';
import { ScheduledTransactionDeleteDialogComponent } from './delete/scheduled-transaction-delete-dialog.component';
import { ScheduledTransactionRoutingModule } from './route/scheduled-transaction-routing.module';
import { IconService } from '../../shared/icon-picker/service/icon.service';
@NgModule({
  imports: [SharedModule, ScheduledTransactionRoutingModule],
  declarations: [
    ScheduledTransactionComponent,
    ScheduledTransactionDetailComponent,
    ScheduledTransactionUpdateComponent,
    ScheduledTransactionDeleteDialogComponent,
  ],
  entryComponents: [ScheduledTransactionDeleteDialogComponent],
  providers: [IconService],
})
export class ScheduledTransactionModule {}
