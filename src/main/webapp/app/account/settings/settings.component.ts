import { AccountDeleteDialogComponent } from './delete/delete-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserDetails } from './../../entities/user-details/user-details.model';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { LANGUAGES } from 'app/config/language.constants';

@Component({
  selector: 'jhi-settings',
  templateUrl: './settings.component.html',
})
export class SettingsComponent implements OnInit {
  account!: Account;
  userDetails!: UserDetails;
  success = false;
  languages = LANGUAGES;
  settingsForm = this.fb.group({
    firstName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    lastName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    email: [undefined, [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    phone: [undefined, [Validators.required]],
    country: [undefined, [Validators.required]],
    apiKey: [
      {
        value: '',
        disabled: true,
      },
    ],
    notifications: [],
    langKey: [undefined],
  });

  constructor(
    private accountService: AccountService,
    private fb: FormBuilder,
    private translateService: TranslateService,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.settingsForm.patchValue({
          firstName: account.firstName,
          lastName: account.lastName,
          email: account.email,
          langKey: account.langKey,
        });

        this.account = account;
      }
    });

    this.accountService.fetchUserData().subscribe(userDetails => {
      this.settingsForm.patchValue({
        country: userDetails.country,
        phone: userDetails.phone,
        notifications: userDetails.notifications,
        apiKey: userDetails.apiKey,
      });
      this.userDetails.country = userDetails.country;
      this.userDetails.phone = userDetails.phone;
      this.userDetails.notifications = userDetails.notifications;
      this.userDetails.apiKey = userDetails.apiKey;
    });
  }

  generateApiKey(): void {
    this.accountService.generateApiKey().subscribe(userDetails => {
      this.settingsForm.patchValue({
        apiKey: userDetails.apiKey,
      });
      this.userDetails.apiKey = userDetails.apiKey;
    });
  }

  deleteApiKey(): void {
    this.accountService.deleteApiKey().subscribe(() => {
      this.settingsForm.patchValue({
        apiKey: '',
      });
      this.userDetails.apiKey = '';
    });
  }

  isApiKeyEmpty(): boolean {
    return !this.settingsForm.get('apiKey')!.value;
  }

  delete(): void {
    const modalRef = this.modalService.open(AccountDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        //cambiar con logout
      }
    });
  }

  save(): void {
    this.success = false;

    this.account.firstName = this.settingsForm.get('firstName')!.value;
    this.account.lastName = this.settingsForm.get('lastName')!.value;
    this.account.email = this.settingsForm.get('email')!.value;
    this.account.langKey = this.settingsForm.get('langKey')!.value;
    this.account.phone = this.settingsForm.get('phone')!.value;
    this.account.country = this.settingsForm.get('country')!.value;
    this.account.notifications = this.settingsForm.get('notifications')!.value;
    this.account.apiKey = this.settingsForm.get('apiKey')?.value;
    this.accountService.save(this.account).subscribe(() => {
      this.success = true;

      this.accountService.authenticate(this.account);

      if (this.account.langKey !== this.translateService.currentLang) {
        this.translateService.use(this.account.langKey);
      }
    });
  }
}
