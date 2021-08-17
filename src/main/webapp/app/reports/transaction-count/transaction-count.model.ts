export interface ITransactionCount {
  count?: number;
  movementType?: string;
}

export class TransactionCount implements ITransactionCount {
  constructor(public count?: number, public movementType?: string) {}
}
