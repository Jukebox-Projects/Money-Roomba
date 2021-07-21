import { Validators, FormBuilder } from '@angular/forms';
import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILicense } from '../license.model';
import { LicenseService } from '../service/license.service';

@Component({
  templateUrl: './license-buy-or-activate-dialog.component.html',
})
export class LicenseBuyOrActivateDialogComponent {
  license?: ILicense;

  creationForm = this.fb.group({
    code: [null, [Validators.required]],
  });
  constructor(protected licenseService: LicenseService, protected activeModal: NgbActiveModal, protected fb: FormBuilder) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  activate(): void {
    let licenseCode = this.creationForm.get('code').value || 1;
    this.licenseService.activate({ code: licenseCode }).subscribe(() => {
      this.activeModal.close('bought');
    });
  }
}
