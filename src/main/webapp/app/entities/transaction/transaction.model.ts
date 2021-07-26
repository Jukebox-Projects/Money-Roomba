import * as dayjs from 'dayjs';
import { IAttachment } from 'app/entities/attachment/attachment.model';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { ICurrency } from 'app/entities/currency/currency.model';
import { ICategory } from 'app/entities/category/category.model';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { MovementType } from 'app/entities/enumerations/movement-type.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { TransactionState } from 'app/entities/enumerations/transaction-state.model';

export interface ITransaction {
  id?: number;
  name?: string;
  description?: string | null;
  dateAdded?: dayjs.Dayjs;
  amount?: number | null;
  originalAmount?: number;
  movementType?: MovementType;
  scheduled?: boolean;
  addToReports?: boolean;
  incomingTransaction?: boolean;
  transactionType?: TransactionType;
  state?: TransactionState | null;
  attachment?: IAttachment | null;
  wallet?: IWallet;
  currency?: ICurrency | null;
  category?: ICategory | null;
  sourceUser?: IUserDetails | null;
  recievingUser?: IUserDetails | null;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public dateAdded?: dayjs.Dayjs,
    public amount?: number | null,
    public originalAmount?: number,
    public movementType?: MovementType,
    public scheduled?: boolean,
    public addToReports?: boolean,
    public incomingTransaction?: boolean,
    public transactionType?: TransactionType,
    public state?: TransactionState | null,
    public attachment?: IAttachment | null,
    public wallet?: IWallet,
    public currency?: ICurrency | null,
    public category?: ICategory | null,
    public sourceUser?: IUserDetails | null,
    public recievingUser?: IUserDetails | null
  ) {
    this.scheduled = this.scheduled ?? false;
    this.addToReports = this.addToReports ?? false;
    this.incomingTransaction = this.incomingTransaction ?? false;
  }
}

export function getTransactionIdentifier(transaction: ITransaction): number | undefined {
  return transaction.id;
}
