import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IScheduledTransaction } from '../scheduled-transaction.model';
import { ScheduledTransactionService } from '../service/scheduled-transaction.service';

@Component({
  templateUrl: './scheduled-transaction-delete-dialog.component.html',
})
export class ScheduledTransactionDeleteDialogComponent {
  scheduledTransaction?: IScheduledTransaction;

  constructor(protected scheduledTransactionService: ScheduledTransactionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.scheduledTransactionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
