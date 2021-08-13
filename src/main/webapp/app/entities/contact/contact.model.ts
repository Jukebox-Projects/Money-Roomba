export interface IContact {
  id?: number;
  name?: string;
  email?: string;
  phone?: string;
}

export class Contact implements IContact {
  constructor(public id?: number, public name?: string, public email?: string, public phone?: string) {}
}

export function getContactIdentifier(contact: IContact): number | undefined {
  return contact.id;
}
