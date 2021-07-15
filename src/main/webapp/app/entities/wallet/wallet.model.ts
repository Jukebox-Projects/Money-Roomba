import { ITransaction } from 'app/entities/transaction/transaction.model';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { ICurrency } from 'app/entities/currency/currency.model';

export interface IWallet {
  id?: number;
  name?: string;
  description?: string | null;
  inReports?: boolean;
  isActive?: boolean;
  balance?: number;
  icon?: number | null;
  transactions?: ITransaction[] | null;
  user?: IUserDetails;
  currency?: ICurrency | null;
}

export class Wallet implements IWallet {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public inReports?: boolean,
    public isActive?: boolean,
    public balance?: number,
    public icon?: number | null,
    public transactions?: ITransaction[] | null,
    public user?: IUserDetails,
    public currency?: ICurrency | null
  ) {
    this.inReports = this.inReports ?? false;
    this.isActive = this.isActive ?? false;
  }
}

export function getWalletIdentifier(wallet: IWallet): number | undefined {
  return wallet.id;
}
