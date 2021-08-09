import { ICurrency } from '../../entities/currency/currency.model';
import { ICategory } from '../../entities/category/category.model';

export interface ITransactionsByCategory {
  total?: number;
  category?: ICategory;
  wallet?: number;
  movementType?: string;
  currency?: ICurrency;
}

export class TransactionsByCategoryModel implements ITransactionsByCategory {
  constructor(
    public total?: number,
    public category?: ICategory,
    public wallet?: number,
    public movementType?: string,
    public currency?: ICurrency
  ) {}
}
