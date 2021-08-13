export interface IImportedTransactionCount {
  count?: number;
}

export class ImportedTransactionCount implements IImportedTransactionCount {
  constructor(public count?: number) {}
}
