import {Component, OnInit} from '@angular/core';
import {UserService} from '../../service/user.service';
import {CookieService} from 'ngx-cookie-service';
import {CookieHolder} from '../../model/CookieHolder';
import {TokenService} from '../../service/token/token.service';
import {UserUpdateDto} from '../../model/UserUpdateDto';

@Component({
  selector: 'app-user-edition',
  templateUrl: './user-edition.component.html',
  styleUrls: ['./user-edition.component.css']
})
export class UserEditionComponent implements OnInit {

  formModel: UserUpdateDto;
  isEmailDisabled = true;
  isPasswordDisabled = true;
  requestState = 'none';
  newPasswordConfirm: string;
  oldEmail: string;
  username: string;
  email: string;

  myCallbackFunction = (): void => {

  }

  onSubmit() {
    if (this.isPasswordDisabled){
      this.formModel.newPassword = this.formModel.password;
    }
    this.userService.updateUserInfo(this.formModel).then(() => {
      this.requestState = 'success';
      this.ngOnInit();
    })
      .catch(() => {
        this.requestState = 'failure';
        this.ngOnInit();
      });
    this.isEmailDisabled = true;
    this.isPasswordDisabled = true;
    setTimeout(() => {
      this.requestState = 'none';
    }, 3500);
  }

  constructor(private userService: UserService, private cookieService: CookieService,
              private tokenService: TokenService) {}

  prepareUser() {
    this.newPasswordConfirm = '';
    this.tokenService.getUserInfo(this.cookieService).toPromise().then(response => {
      this.email = response.email;
      this.oldEmail = this.email;
      this.username = this.cookieService.get(CookieHolder.usernameKey);
      this.formModel = new UserUpdateDto(this.username, '', '', this.email);
    });
  }

  toggleEmailEdit() {
    this.isEmailDisabled = !this.isEmailDisabled;
    this.toggleEdit();
  }

  toggleEdit(){
    this.newPasswordConfirm = '';
    this.formModel = new UserUpdateDto(this.formModel.username, '', '', this.oldEmail);
  }

  togglePasswordEdit(){
    this.isPasswordDisabled = !this.isPasswordDisabled;
    this.toggleEdit();
  }

  ngOnInit(): void {
    this.prepareUser();
  }
}
