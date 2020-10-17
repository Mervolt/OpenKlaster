import {Component, OnInit} from '@angular/core';
import {UserService} from "../../service/user.service";
import {User} from "../../model/User";
import {CookieService} from "ngx-cookie-service";
import {CookieHolder} from "../../model/CookieHolder";
import {TokenService} from "../../service/token/token.service";
import {UserUpdateDto} from "../../model/UserUpdateDto";

@Component({
  selector: 'app-user-edition',
  templateUrl: './user-edition.component.html',
  styleUrls: ['./user-edition.component.css']
})
export class UserEditionComponent implements OnInit {

  formModel: UserUpdateDto;
  isFormDisabled = true;
  requestState: string = 'none'
  newPasswordConfirm: string;
  oldEmail: string;

  myCallbackFunction = (): void => {
    this.userService.updateUserInfo(this.formModel).then(() => {
      this.requestState = 'success'
      this.ngOnInit()
    })
      .catch(() => {
        this.requestState = 'failure'
        this.ngOnInit()
      })
    setTimeout(() => {
      this.requestState = 'none'
    }, 3500);
  }

  constructor(private userService: UserService, private cookieService: CookieService,
              private tokenService: TokenService) {}

  prepareUser() {
    this.newPasswordConfirm = ''
    this.tokenService.getTokens(this.cookieService).toPromise().then(response => {
      const email = response['email'];
      this.oldEmail = email;
      const username = this.cookieService.get(CookieHolder.usernameKey);
      this.formModel = new UserUpdateDto(username, "", "", email)
    })
  }

  toggleEdition() {
    this.isFormDisabled = !this.isFormDisabled
    this.newPasswordConfirm = ''
    this.formModel = new UserUpdateDto(this.formModel.username, '', '', this.oldEmail);
  }

  ngOnInit(): void {
    this.prepareUser();
  }
}
