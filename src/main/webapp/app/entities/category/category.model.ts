import { ITransaction } from 'app/entities/transaction/transaction.model';
import { IUserDetails } from 'app/entities/user-details/user-details.model';

export interface ICategory {
  id?: number;
  name?: string;
  isActive?: boolean;
  userCreated?: boolean;
  icon?: number | null;
  categories?: ICategory[] | null;
  transactions?: ITransaction[] | null;
  parent?: ICategory | null;
  user?: IUserDetails | null;
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public name?: string,
    public isActive?: boolean,
    public userCreated?: boolean,
    public icon?: number | null,
    public categories?: ICategory[] | null,
    public transactions?: ITransaction[] | null,
    public parent?: ICategory | null,
    public user?: IUserDetails | null
  ) {
    this.isActive = this.isActive ?? false;
    this.userCreated = this.userCreated ?? false;
  }
}

export function getCategoryIdentifier(category: ICategory): number | undefined {
  return category.id;
}
