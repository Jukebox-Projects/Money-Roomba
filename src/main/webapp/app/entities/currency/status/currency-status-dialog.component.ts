import { Component } from '@angular/core';
import { ICurrency } from '../currency.model';
import { CurrencyService } from '../service/currency.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  templateUrl: './currency-status-dialog.component.html',
})
export class CurrencyStatusDialogComponent {
  currency?: ICurrency;

  constructor(protected currencyService: CurrencyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmChange(id: number): void {
    this.currencyService.statusUpdate(id).subscribe(() => {
      this.activeModal.close('status changed');
    });
  }
}
