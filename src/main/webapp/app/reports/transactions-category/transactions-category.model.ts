import { ICurrency } from '../../entities/currency/currency.model';
import { ICategory } from '../../entities/category/category.model';

export interface ITransactionsByCategory {
  total?: number;
  count?: number;
  category?: ICategory;
  movementType?: string;
  currency?: ICurrency;
}

export class TransactionsByCategoryModel implements ITransactionsByCategory {
  constructor(
    public total?: number,
    public count?: number,
    public category?: ICategory,
    public movementType?: string,
    public currency?: ICurrency
  ) {}
}
