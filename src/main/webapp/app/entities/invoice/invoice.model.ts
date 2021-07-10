import * as dayjs from 'dayjs';
import { ICurrency } from 'app/entities/currency/currency.model';

export interface IInvoice {
  id?: number;
  companyName?: string;
  userName?: string;
  userLastName?: string;
  userEmail?: string;
  dateCreated?: dayjs.Dayjs;
  total?: number;
  subTotal?: number;
  tax?: number | null;
  purchaseDescription?: string | null;
  itemQuantity?: number;
  itemPrice?: number;
  currency?: ICurrency | null;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public companyName?: string,
    public userName?: string,
    public userLastName?: string,
    public userEmail?: string,
    public dateCreated?: dayjs.Dayjs,
    public total?: number,
    public subTotal?: number,
    public tax?: number | null,
    public purchaseDescription?: string | null,
    public itemQuantity?: number,
    public itemPrice?: number,
    public currency?: ICurrency | null
  ) {}
}

export function getInvoiceIdentifier(invoice: IInvoice): number | undefined {
  return invoice.id;
}
