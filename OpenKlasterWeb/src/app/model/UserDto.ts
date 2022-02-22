export class UserDto {
  constructor(public username: string, public email: string, public password: string, public role: string, public active: boolean) {
  }
}
