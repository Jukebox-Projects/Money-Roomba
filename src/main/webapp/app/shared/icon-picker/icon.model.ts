export interface IICon {
  id?: number;
  name?: string;
  path?: string;
}

export class Icon implements IICon {
  constructor(public id?: number, public name?: string, public path?: string) {}
}

export function getIconIdentifier(icon: IICon): number | undefined {
  return icon.id;
}
