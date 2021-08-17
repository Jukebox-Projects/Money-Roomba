import { AccountService } from 'app/core/auth/account.service';
import { UserManagementService } from './../../../admin/user-management/service/user-management.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Authority } from 'app/config/authority.constants';
import { COUNTRYLIST } from './../../../shared/country';
import { LANGUAGES } from 'app/config/language.constants';
import { Account } from 'app/core/auth/account.model';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserDetails } from '../user-details.model';
import { User } from 'app/admin/user-management/user-management.model';

@Component({
  selector: 'jhi-user-details-detail',
  templateUrl: './user-details-detail.component.html',
})
export class UserDetailsDetailComponent implements OnInit {
  account!: Account;
  userDetails!: IUserDetails;
  success = false;
  languages = LANGUAGES;
  countryList = COUNTRYLIST;
  authority = Authority;
  user!: User;
  authorities: string[] = [];
  isSaving = false;
  active: any = 1;

  editForm = this.fb.group({
    id: [],
    firstName: ['', [Validators.maxLength(50)]],
    lastName: ['', [Validators.maxLength(50)]],
    phone: [null, [Validators.required, Validators.pattern(/\(?([0-9]{3})\)?([ .-]?)([0-9]{4})\2([0-9]{4})/)]],
    country: [this.countryList[0].value, [Validators.required]],
    email: ['', [Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    notifications: [],
    activated: [],
    langKey: [],
    authorities: [],
  });

  constructor(
    private fb: FormBuilder,
    private translateService: TranslateService,
    protected modalService: NgbModal,
    private accountService: AccountService,
    protected route: ActivatedRoute,
    protected userManagementService: UserManagementService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ account }) => {
      this.account = account;
      this.route.queryParams.subscribe(params => {
        this.active = Number(params['active']) ? Number(params['active']) : 1;

        if (account) {
          this.user = account;
          this.user.login = this.user.email;

          if (this.user.id === undefined) {
            this.user.activated = true;
          } else {
            this.userManagementService.findUserDetails(account.email).subscribe(userDetail => {
              this.user.country = userDetail.country;
              this.user.phone = userDetail.phone;
              this.user.notifications = userDetail.notifications;
              this.updateForm(account);
            });
          }
          this.updateForm(account);

          this.account = account;
        }
      });
    });
    this.userManagementService.authorities().subscribe(authorities => (this.authorities = authorities));
  }

  previousState(): void {
    window.history.back();
  }

  getCountry(): string {
    if (this.account && this.account?.country) {
      return this.countryList.find(x => x.value === this.account.country)?.name;
    } else {
      return '';
    }
  }

  enumAuthorities(rol: string): string {
    return Object.keys(this.authority)
      .find(key => this.authority[key] === rol)
      .replace('_', ' ');
  }

  private updateForm(user: User): void {
    this.editForm.patchValue({
      id: user.id,
      login: user.login,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      activated: user.activated,
      langKey: user.langKey,
      authorities: user.authorities,
      phone: user.phone,
      country: user.country,
      notifications: user.notifications,
    });
  }

  private updateUser(user: User): void {
    user.login = this.editForm.get(['email'])!.value;
    user.firstName = this.editForm.get(['firstName'])!.value;
    user.lastName = this.editForm.get(['lastName'])!.value;
    user.email = this.editForm.get(['email'])!.value;
    user.activated = this.editForm.get(['activated'])!.value;
    user.langKey = this.editForm.get(['langKey'])!.value;
    user.authorities = this.editForm.get(['authorities'])!.value;
    user.phone = this.editForm.get(['phone'])!.value;
    user.country = this.editForm.get(['country'])!.value;
    user.notifications = this.editForm.get(['notifications'])!.value;
    if (!this.editForm.get(['notifications'])!.value) {
      user.notifications = false;
    }
  }

  save(): void {
    this.isSaving = true;
    this.updateUser(this.user);
    if (this.user.id !== undefined) {
      this.userManagementService.update(this.user).subscribe(
        () => this.onSaveSuccess(),
        () => this.onSaveError()
      );
    } else {
      this.userManagementService.create(this.user).subscribe(
        () => this.onSaveSuccess(),
        () => this.onSaveError()
      );
    }
  }
  private onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError(): void {
    this.isSaving = false;
  }
}
