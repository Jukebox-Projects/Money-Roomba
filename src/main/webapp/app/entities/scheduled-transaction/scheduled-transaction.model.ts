import * as dayjs from 'dayjs';
import { ISchedulePattern } from 'app/entities/schedule-pattern/schedule-pattern.model';
import { ICurrency } from 'app/entities/currency/currency.model';
import { MovementType } from 'app/entities/enumerations/movement-type.model';

export interface IScheduledTransaction {
  id?: number;
  name?: string;
  description?: string | null;
  originalAmount?: number;
  movementType?: MovementType;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs | null;
  addToReports?: boolean;
  incomingTransaction?: boolean;
  schedulePatterns?: ISchedulePattern[] | null;
  currency?: ICurrency | null;
}

export class ScheduledTransaction implements IScheduledTransaction {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public originalAmount?: number,
    public movementType?: MovementType,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs | null,
    public addToReports?: boolean,
    public incomingTransaction?: boolean,
    public schedulePatterns?: ISchedulePattern[] | null,
    public currency?: ICurrency | null
  ) {
    this.addToReports = this.addToReports ?? false;
    this.incomingTransaction = this.incomingTransaction ?? false;
  }
}

export function getScheduledTransactionIdentifier(scheduledTransaction: IScheduledTransaction): number | undefined {
  return scheduledTransaction.id;
}
