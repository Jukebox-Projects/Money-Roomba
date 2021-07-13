import { AccountService } from './../../../core/auth/account.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { LANGUAGES } from 'app/config/language.constants';
import { User } from '../user-management.model';
import { UserManagementService } from '../service/user-management.service';

@Component({
  selector: 'jhi-user-mgmt-update',
  templateUrl: './user-management-update.component.html',
})
export class UserManagementUpdateComponent implements OnInit {
  user!: User;
  languages = LANGUAGES;
  authorities: string[] = [];
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    firstName: ['', [Validators.maxLength(50)]],
    lastName: ['', [Validators.maxLength(50)]],
    phone: ['', [Validators.required]],
    country: ['', [Validators.required]],
    email: ['', [Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    notifications: [],
    activated: [],
    langKey: [],
    authorities: [],
  });

  constructor(private userService: UserManagementService, private route: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.route.data.subscribe(({ user }) => {
      if (user) {
        this.user = user;
        this.user.login = this.user.email;

        if (this.user.id === undefined) {
          this.user.activated = true;
        } else {
          this.userService.findUserDetails(user.email).subscribe(userDetails => {
            this.user.country = userDetails.country;
            this.user.phone = userDetails.phone;
            this.user.notifications = userDetails.notifications;
            this.updateForm(user);
          });
        }
        this.updateForm(user);
      }
    });
    this.userService.authorities().subscribe(authorities => (this.authorities = authorities));
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.updateUser(this.user);
    if (this.user.id !== undefined) {
      this.userService.update(this.user).subscribe(
        () => this.onSaveSuccess(),
        () => this.onSaveError()
      );
    } else {
      this.userService.create(this.user).subscribe(
        () => this.onSaveSuccess(),
        () => this.onSaveError()
      );
    }
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
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError(): void {
    this.isSaving = false;
  }
}
