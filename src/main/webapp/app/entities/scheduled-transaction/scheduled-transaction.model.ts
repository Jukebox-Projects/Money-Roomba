import * as dayjs from 'dayjs';
import { ICurrency } from 'app/entities/currency/currency.model';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { ICategory } from 'app/entities/category/category.model';
import { MovementType } from 'app/entities/enumerations/movement-type.model';
import { RecurringType } from 'app/entities/enumerations/recurring-type.model';
import { IWallet } from 'app/entities/wallet/wallet.model';

export interface IScheduledTransaction {
  id?: number;
  name?: string;
  description?: string | null;
  originalAmount?: number;
  movementType?: MovementType;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs | null;
  addToReports?: boolean;
  recurringType?: RecurringType;
  separationCount?: number | null;
  maxNumberOfOcurrences?: number | null;
  dayOfWeek?: number | null;
  weekOfMonth?: number | null;
  dayOfMonth?: number | null;
  monthOfYear?: number | null;
  currency?: ICurrency | null;
  sourceUser?: IUserDetails | null;
  category?: ICategory | null;
  wallet?: IWallet;
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
    public recurringType?: RecurringType,
    public separationCount?: number | null,
    public maxNumberOfOcurrences?: number | null,
    public dayOfWeek?: number | null,
    public weekOfMonth?: number | null,
    public dayOfMonth?: number | null,
    public monthOfYear?: number | null,
    public currency?: ICurrency | null,
    public sourceUser?: IUserDetails | null,
    public category?: ICategory | null,
    public wallet?: IWallet
  ) {
    this.addToReports = this.addToReports ?? false;
  }
}

export function getScheduledTransactionIdentifier(scheduledTransaction: IScheduledTransaction): number | undefined {
  return scheduledTransaction.id;
}
