import { AccountService } from 'app/core/auth/account.service';
import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginService } from '../../../login/login.service';
import { Router } from '@angular/router';

@Component({
  templateUrl: './delete-dialog.component.html',
})
export class AccountDeleteDialogComponent {
  constructor(
    private router: Router,
    protected accountService: AccountService,
    private loginService: LoginService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(): void {
    this.accountService.delete().subscribe(() => {
      this.activeModal.close('deleted');
      this.loginService.logout();
      this.router.navigate(['/login']);
    });
  }
}
