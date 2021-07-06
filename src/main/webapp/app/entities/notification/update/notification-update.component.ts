import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { INotification, Notification } from '../notification.model';
import { NotificationService } from '../service/notification.service';

@Component({
  selector: 'jhi-notification-update',
  templateUrl: './notification-update.component.html',
})
export class NotificationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    dateOpened: [null, [Validators.required]],
    opened: [null, [Validators.required]],
  });

  constructor(protected notificationService: NotificationService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notification }) => {
      this.updateForm(notification);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notification = this.createFromForm();
    if (notification.id !== undefined) {
      this.subscribeToSaveResponse(this.notificationService.update(notification));
    } else {
      this.subscribeToSaveResponse(this.notificationService.create(notification));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotification>>): void {
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

  protected updateForm(notification: INotification): void {
    this.editForm.patchValue({
      id: notification.id,
      dateOpened: notification.dateOpened,
      opened: notification.opened,
    });
  }

  protected createFromForm(): INotification {
    return {
      ...new Notification(),
      id: this.editForm.get(['id'])!.value,
      dateOpened: this.editForm.get(['dateOpened'])!.value,
      opened: this.editForm.get(['opened'])!.value,
    };
  }
}
