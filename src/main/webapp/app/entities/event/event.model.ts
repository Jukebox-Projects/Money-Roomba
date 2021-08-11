import * as dayjs from 'dayjs';
import { INotification } from 'app/entities/notification/notification.model';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { EventType } from 'app/entities/enumerations/event-type.model';
import { SourceEntity } from 'app/entities/enumerations/source-entity.model';

export interface IEvent {
  id?: number;
  eventType?: EventType;
  dateAdded?: dayjs.Dayjs;
  sourceId?: number;
  sourceEntity?: SourceEntity;
  userName?: string;
  userLastName?: string;
  notification?: INotification | null;
  user?: IUserDetails | null;
  destinationPath?: string | null;
  message?: string | null;
}

export class Event implements IEvent {
  constructor(
    public id?: number,
    public eventType?: EventType,
    public dateAdded?: dayjs.Dayjs,
    public sourceId?: number,
    public sourceEntity?: SourceEntity,
    public userName?: string,
    public userLastName?: string,
    public notification?: INotification | null,
    public user?: IUserDetails | null,
    public destinationPath?: string | null,
    public message?: string | null
  ) {}
}

export function getEventIdentifier(event: IEvent): number | undefined {
  return event.id;
}
