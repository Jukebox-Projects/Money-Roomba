export class AccountDetail {
  constructor(
    public isTemporaryPassword: boolean | false,
    public country: string,
    public isActive: string | true,
    public phone: string,
    public notifications: boolean | true
  ) {}
}
