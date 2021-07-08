export class Registration {
  constructor(
    public email: string,
    public password: string,
    public firstName: string,
    public lastName: string,
    public country: string,
    public phone: string,
    public langKey: string
  ) {}
}
