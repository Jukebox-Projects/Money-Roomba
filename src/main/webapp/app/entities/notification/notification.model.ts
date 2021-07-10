import * as dayjs from 'dayjs';
import { IEvent } from 'app/entities/event/event.model';

export interface INotification {
  id?: number;
  dateOpened?: dayjs.Dayjs;
  opened?: boolean;
  event?: IEvent | null;
}

export class Notification implements INotification {
  constructor(public id?: number, public dateOpened?: dayjs.Dayjs, public opened?: boolean, public event?: IEvent | null) {
    this.opened = this.opened ?? false;
  }
}

export function getNotificationIdentifier(notification: INotification): number | undefined {
  return notification.id;
}
