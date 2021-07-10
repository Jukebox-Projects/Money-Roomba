import { ITransaction } from 'app/entities/transaction/transaction.model';
import { IScheduledTransaction } from 'app/entities/scheduled-transaction/scheduled-transaction.model';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';

export interface ICurrency {
  id?: number;
  code?: string;
  name?: string;
  conversionRate?: number;
  transactions?: ITransaction[] | null;
  scheduledTransactions?: IScheduledTransaction[] | null;
  wallets?: IWallet[] | null;
  invoices?: IInvoice[] | null;
}

export class Currency implements ICurrency {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public conversionRate?: number,
    public transactions?: ITransaction[] | null,
    public scheduledTransactions?: IScheduledTransaction[] | null,
    public wallets?: IWallet[] | null,
    public invoices?: IInvoice[] | null
  ) {}
}

export function getCurrencyIdentifier(currency: ICurrency): number | undefined {
  return currency.id;
}
