import { IScheduledTransaction } from 'app/entities/scheduled-transaction/scheduled-transaction.model';
import { RecurringType } from 'app/entities/enumerations/recurring-type.model';

export interface ISchedulePattern {
  id?: number;
  recurringType?: RecurringType;
  separationCount?: number | null;
  maxNumberOfOcurrences?: number | null;
  dayOfWeek?: number | null;
  weekOfMonth?: number | null;
  dayOfMonth?: number | null;
  monthOfYear?: number | null;
  scheduleTransaction?: IScheduledTransaction | null;
}

export class SchedulePattern implements ISchedulePattern {
  constructor(
    public id?: number,
    public recurringType?: RecurringType,
    public separationCount?: number | null,
    public maxNumberOfOcurrences?: number | null,
    public dayOfWeek?: number | null,
    public weekOfMonth?: number | null,
    public dayOfMonth?: number | null,
    public monthOfYear?: number | null,
    public scheduleTransaction?: IScheduledTransaction | null
  ) {}
}

export function getSchedulePatternIdentifier(schedulePattern: ISchedulePattern): number | undefined {
  return schedulePattern.id;
}
