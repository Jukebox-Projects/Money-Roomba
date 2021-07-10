export interface ISystemSetting {
  id?: number;
  key?: string;
  value?: number;
}

export class SystemSetting implements ISystemSetting {
  constructor(public id?: number, public key?: string, public value?: number) {}
}

export function getSystemSettingIdentifier(systemSetting: ISystemSetting): number | undefined {
  return systemSetting.id;
}
