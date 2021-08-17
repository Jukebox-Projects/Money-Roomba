import { Component, OnInit } from '@angular/core';
import { IEvent } from '../../entities/event/event.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EventService } from '../../entities/event/service/event.service';
import { HttpResponse } from '@angular/common/http';
import { INotification } from '../../entities/notification/notification.model';
import { NotificationService } from '../../entities/notification/service/notification.service';

@Component({
  templateUrl: './notifications-dialog.component.html',
  styleUrls: ['./notifications-dialog.component.scss'],
})
export class NotificationsDialogComponent {
  eventsNotifications?: IEvent[];

  constructor(
    protected eventService: EventService,
    protected activeModal: NgbActiveModal,
    protected notificationService: NotificationService
  ) {}

  close(): void {
    this.activeModal.dismiss();
  }

  getNotifications(): void {
    this.eventService.queryUserAllNotifications().subscribe((res: HttpResponse<IEvent[]>) => {
      this.eventsNotifications = res.body ?? [];
    });
  }

  updateNotification(notification: INotification): void {
    this.notificationService.seenUpdate(notification).subscribe(() => {
      this.activeModal.close('notification updated');
      this.getNotifications();
    });
  }
}
