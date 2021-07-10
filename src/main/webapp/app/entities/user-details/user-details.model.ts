import { IUser } from 'app/entities/user/user.model';
import { ILicense } from 'app/entities/license/license.model';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { ICategory } from 'app/entities/category/category.model';
import { IEvent } from 'app/entities/event/event.model';
import { ITransaction } from 'app/entities/transaction/transaction.model';

export interface IUserDetails {
  id?: number;
  country?: string;
  phone?: string;
  apiKey?: string | null;
  notifications?: boolean;
  isActive?: boolean;
  isTemporaryPassword?: boolean;
  internalUser?: IUser | null;
  license?: ILicense | null;
  wallets?: IWallet[] | null;
  categories?: ICategory[] | null;
  events?: IEvent[] | null;
  transactions?: ITransaction[] | null;
  userDetails?: IUserDetails[] | null;
  contact?: IUserDetails | null;
}

export class UserDetails implements IUserDetails {
  constructor(
    public id?: number,
    public country?: string,
    public phone?: string,
    public apiKey?: string | null,
    public notifications?: boolean,
    public isActive?: boolean,
    public isTemporaryPassword?: boolean,
    public internalUser?: IUser | null,
    public license?: ILicense | null,
    public wallets?: IWallet[] | null,
    public categories?: ICategory[] | null,
    public events?: IEvent[] | null,
    public transactions?: ITransaction[] | null,
    public userDetails?: IUserDetails[] | null,
    public contact?: IUserDetails | null
  ) {
    this.notifications = this.notifications ?? false;
    this.isActive = this.isActive ?? false;
    this.isTemporaryPassword = this.isTemporaryPassword ?? false;
  }
}

export function getUserDetailsIdentifier(userDetails: IUserDetails): number | undefined {
  return userDetails.id;
}
