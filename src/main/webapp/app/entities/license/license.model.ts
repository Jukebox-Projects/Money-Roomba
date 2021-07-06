import { IInvoice } from 'app/entities/invoice/invoice.model';
import { LicenseCreateMethod } from 'app/entities/enumerations/license-create-method.model';

export interface ILicense {
  id?: number;
  code?: string;
  isAssigned?: boolean;
  isActive?: boolean;
  createMethod?: LicenseCreateMethod;
  invoice?: IInvoice | null;
}

export class License implements ILicense {
  constructor(
    public id?: number,
    public code?: string,
    public isAssigned?: boolean,
    public isActive?: boolean,
    public createMethod?: LicenseCreateMethod,
    public invoice?: IInvoice | null
  ) {
    this.isAssigned = this.isAssigned ?? false;
    this.isActive = this.isActive ?? false;
  }
}

export function getLicenseIdentifier(license: ILicense): number | undefined {
  return license.id;
}
