export class UserUpdateDto {
  constructor(public username: string, public password: string, public newPassword: string, public email: string) {
  }
}
