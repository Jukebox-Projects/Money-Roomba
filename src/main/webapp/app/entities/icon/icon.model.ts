import { ICategory } from 'app/entities/category/category.model';
import { IWallet } from 'app/entities/wallet/wallet.model';

export interface IIcon {
  id?: number;
  url?: string;
  name?: string;
  categories?: ICategory[] | null;
  wallets?: IWallet[] | null;
}

export class Icon implements IIcon {
  constructor(
    public id?: number,
    public url?: string,
    public name?: string,
    public categories?: ICategory[] | null,
    public wallets?: IWallet[] | null
  ) {}
}

export function getIconIdentifier(icon: IIcon): number | undefined {
  return icon.id;
}
