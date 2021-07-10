import { AccountService } from 'app/core/auth/account.service';
import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  templateUrl: './delete-dialog.component.html',
})
export class AccountDeleteDialogComponent {
  constructor(protected accountService: AccountService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(): void {
    this.accountService.delete().subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
