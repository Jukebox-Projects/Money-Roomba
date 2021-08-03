export interface IStartEndDate {
  startDate?: Date;
  endDate?: Date;
}

export class startEndDateModel implements IStartEndDate {
  constructor(public startDate?: Date, public endDate?: Date) {}
}
