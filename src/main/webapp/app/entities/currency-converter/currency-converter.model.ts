import { ICurrency } from '../currency/currency.model';

export interface ICurrencyConverter {
  sourceCurrency?: ICurrency | null;
  targetCurrency?: ICurrency | null;
  amount?: number | 0;
  result?: string | 0;
  conversionRate?: number | 0;
}

export class CurrencyConverter implements ICurrencyConverter {
  constructor(
    public sourceCurrency?: ICurrency | null,
    public targetCurrency?: ICurrency | null,
    public amount?: number | 0,
    public result?: string | 0,
    conversionRate?: number | 0
  ) {}
}
