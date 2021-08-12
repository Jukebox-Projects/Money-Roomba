import { Validators, FormBuilder } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ContactService } from '../../service/contact.service';

@Component({
  selector: 'jhi-add-contact-modal',
  templateUrl: './add-contact-modal.component.html',
  styleUrls: ['./add-contact-modal.component.scss'],
})
export class AddContactModalComponent implements OnInit {
  creationForm = this.fb.group({
    identifier: [[], [Validators.required]],
  });

  constructor(protected contactService: ContactService, protected activeModal: NgbActiveModal, protected fb: FormBuilder) {}

  ngOnInit(): void {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  addContact(): void {
    let identifier = this.creationForm.get('identifier').value || '';
    /* eslint no-console: "warn" */
    console.log(identifier);
    this.contactService.create(identifier).subscribe(() => {
      this.activeModal.close('activated');
    });
  }
}
