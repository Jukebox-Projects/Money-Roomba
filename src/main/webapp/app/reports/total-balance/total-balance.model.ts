import { ICurrency } from '../../entities/currency/currency.model';

export interface ITotalBalance {
  total?: number;
  currency?: ICurrency;
}

export class TotalBalanceModel implements ITotalBalance {
  constructor(public total?: number, public currency?: ICurrency) {}
}
