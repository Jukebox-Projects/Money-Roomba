import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEvent, Event } from '../event.model';
import { EventService } from '../service/event.service';
import { INotification } from 'app/entities/notification/notification.model';
import { NotificationService } from 'app/entities/notification/service/notification.service';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';

@Component({
  selector: 'jhi-event-update',
  templateUrl: './event-update.component.html',
})
export class EventUpdateComponent implements OnInit {
  isSaving = false;

  notificationsCollection: INotification[] = [];
  userDetailsSharedCollection: IUserDetails[] = [];

  editForm = this.fb.group({
    id: [],
    eventType: [null, [Validators.required]],
    dateAdded: [null, [Validators.required]],
    sourceId: [null, [Validators.required]],
    sourceEntity: [null, [Validators.required]],
    userName: [null, [Validators.required]],
    userLastName: [null, [Validators.required]],
    notification: [],
    user: [],
  });

  constructor(
    protected eventService: EventService,
    protected notificationService: NotificationService,
    protected userDetailsService: UserDetailsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ event }) => {
      this.updateForm(event);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const event = this.createFromForm();
    if (event.id !== undefined) {
      this.subscribeToSaveResponse(this.eventService.update(event));
    } else {
      this.subscribeToSaveResponse(this.eventService.create(event));
    }
  }

  trackNotificationById(index: number, item: INotification): number {
    return item.id!;
  }

  trackUserDetailsById(index: number, item: IUserDetails): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvent>>): void {
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

  protected updateForm(event: IEvent): void {
    this.editForm.patchValue({
      id: event.id,
      eventType: event.eventType,
      dateAdded: event.dateAdded,
      sourceId: event.sourceId,
      sourceEntity: event.sourceEntity,
      userName: event.userName,
      userLastName: event.userLastName,
      notification: event.notification,
      user: event.user,
    });

    this.notificationsCollection = this.notificationService.addNotificationToCollectionIfMissing(
      this.notificationsCollection,
      event.notification
    );
    this.userDetailsSharedCollection = this.userDetailsService.addUserDetailsToCollectionIfMissing(
      this.userDetailsSharedCollection,
      event.user
    );
  }

  protected loadRelationshipsOptions(): void {
    this.notificationService
      .query({ filter: 'event-is-null' })
      .pipe(map((res: HttpResponse<INotification[]>) => res.body ?? []))
      .pipe(
        map((notifications: INotification[]) =>
          this.notificationService.addNotificationToCollectionIfMissing(notifications, this.editForm.get('notification')!.value)
        )
      )
      .subscribe((notifications: INotification[]) => (this.notificationsCollection = notifications));

    this.userDetailsService
      .query()
      .pipe(map((res: HttpResponse<IUserDetails[]>) => res.body ?? []))
      .pipe(
        map((userDetails: IUserDetails[]) =>
          this.userDetailsService.addUserDetailsToCollectionIfMissing(userDetails, this.editForm.get('user')!.value)
        )
      )
      .subscribe((userDetails: IUserDetails[]) => (this.userDetailsSharedCollection = userDetails));
  }

  protected createFromForm(): IEvent {
    return {
      ...new Event(),
      id: this.editForm.get(['id'])!.value,
      eventType: this.editForm.get(['eventType'])!.value,
      dateAdded: this.editForm.get(['dateAdded'])!.value,
      sourceId: this.editForm.get(['sourceId'])!.value,
      sourceEntity: this.editForm.get(['sourceEntity'])!.value,
      userName: this.editForm.get(['userName'])!.value,
      userLastName: this.editForm.get(['userLastName'])!.value,
      notification: this.editForm.get(['notification'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
