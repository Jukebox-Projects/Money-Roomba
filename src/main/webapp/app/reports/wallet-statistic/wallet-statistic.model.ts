import { ICurrency } from '../../entities/currency/currency.model';

export interface IWalletStatistic {
  balance?: number;
  name?: string;
  curency?: ICurrency;
  percentage?: number;
}

export class WalletStatisticModel implements IWalletStatistic {
  constructor(public balance?: number, public name?: string, public curency?: ICurrency, public percentage?: number) {}
}
