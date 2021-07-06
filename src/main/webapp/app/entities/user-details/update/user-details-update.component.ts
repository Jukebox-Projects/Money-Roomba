import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUserDetails, UserDetails } from '../user-details.model';
import { UserDetailsService } from '../service/user-details.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ILicense } from 'app/entities/license/license.model';
import { LicenseService } from 'app/entities/license/service/license.service';

@Component({
  selector: 'jhi-user-details-update',
  templateUrl: './user-details-update.component.html',
})
export class UserDetailsUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  licensesCollection: ILicense[] = [];
  userDetailsSharedCollection: IUserDetails[] = [];

  editForm = this.fb.group({
    id: [],
    country: [null, [Validators.required]],
    phone: [null, [Validators.required]],
    apiKey: [],
    notifications: [null, [Validators.required]],
    isActive: [null, [Validators.required]],
    isTemporaryPassword: [null, [Validators.required]],
    internalUser: [],
    license: [],
    contact: [],
  });

  constructor(
    protected userDetailsService: UserDetailsService,
    protected userService: UserService,
    protected licenseService: LicenseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userDetails }) => {
      this.updateForm(userDetails);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userDetails = this.createFromForm();
    if (userDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.userDetailsService.update(userDetails));
    } else {
      this.subscribeToSaveResponse(this.userDetailsService.create(userDetails));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackLicenseById(index: number, item: ILicense): number {
    return item.id!;
  }

  trackUserDetailsById(index: number, item: IUserDetails): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserDetails>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userDetails: IUserDetails): void {
    this.editForm.patchValue({
      id: userDetails.id,
      country: userDetails.country,
      phone: userDetails.phone,
      apiKey: userDetails.apiKey,
      notifications: userDetails.notifications,
      isActive: userDetails.isActive,
      isTemporaryPassword: userDetails.isTemporaryPassword,
      internalUser: userDetails.internalUser,
      license: userDetails.license,
      contact: userDetails.contact,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, userDetails.internalUser);
    this.licensesCollection = this.licenseService.addLicenseToCollectionIfMissing(this.licensesCollection, userDetails.license);
    this.userDetailsSharedCollection = this.userDetailsService.addUserDetailsToCollectionIfMissing(
      this.userDetailsSharedCollection,
      userDetails.contact
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('internalUser')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.licenseService
      .query({ 'userDetailsId.specified': 'false' })
      .pipe(map((res: HttpResponse<ILicense[]>) => res.body ?? []))
      .pipe(
        map((licenses: ILicense[]) => this.licenseService.addLicenseToCollectionIfMissing(licenses, this.editForm.get('license')!.value))
      )
      .subscribe((licenses: ILicense[]) => (this.licensesCollection = licenses));

    this.userDetailsService
      .query()
      .pipe(map((res: HttpResponse<IUserDetails[]>) => res.body ?? []))
      .pipe(
        map((userDetails: IUserDetails[]) =>
          this.userDetailsService.addUserDetailsToCollectionIfMissing(userDetails, this.editForm.get('contact')!.value)
        )
      )
      .subscribe((userDetails: IUserDetails[]) => (this.userDetailsSharedCollection = userDetails));
  }

  protected createFromForm(): IUserDetails {
    return {
      ...new UserDetails(),
      id: this.editForm.get(['id'])!.value,
      country: this.editForm.get(['country'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      apiKey: this.editForm.get(['apiKey'])!.value,
      notifications: this.editForm.get(['notifications'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      isTemporaryPassword: this.editForm.get(['isTemporaryPassword'])!.value,
      internalUser: this.editForm.get(['internalUser'])!.value,
      license: this.editForm.get(['license'])!.value,
      contact: this.editForm.get(['contact'])!.value,
    };
  }
}
