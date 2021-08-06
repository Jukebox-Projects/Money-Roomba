import { ICurrency } from '../../entities/currency/currency.model';

export interface IWalletBalance {
  total?: number;
  movementType?: string;
  wallet?: number;
  currency?: ICurrency;
}

export class WalletBalanceModel implements IWalletBalance {
  constructor(public total?: number, public movementType?: string, public wallet?: number, public currency?: ICurrency) {}
}
