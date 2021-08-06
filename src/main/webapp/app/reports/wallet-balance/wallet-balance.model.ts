export interface IWalletBalance {
  total?: number;
  movementType?: string;
  wallet?: number;
}

export class WalletBalanceModel implements IWalletBalance {
  constructor(public total?: number, public movementType?: string, public wallet?: number) {}
}
