import { Validators, FormBuilder } from '@angular/forms';
import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILicense } from '../license.model';
import { LicenseService } from '../service/license.service';

@Component({
  templateUrl: './license-create-dialog.component.html',
})
export class LicenseCreateDialogComponent {
  license?: ILicense;

  creationForm = this.fb.group({
    quantity: [null, [Validators.required, Validators.min(1), Validators.max(50)]],
  });
  constructor(protected licenseService: LicenseService, protected activeModal: NgbActiveModal, protected fb: FormBuilder) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmCreation(): void {
    let qty = this.creationForm.get('quantity').value || 1;
    this.licenseService.create({ quantity: qty }).subscribe(() => {
      this.activeModal.close('created');
    });
  }
}
