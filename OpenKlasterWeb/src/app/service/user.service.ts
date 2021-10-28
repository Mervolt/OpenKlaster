import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../model/User';
import {EndpointHolder} from "../model/EndpointHolder";
import {CookieService} from "ngx-cookie-service";
import {UserUpdateDto} from "../model/UserUpdateDto";
import {ToastrService} from 'ngx-toastr';
import {TranslateService} from "@ngx-translate/core";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(public http: HttpClient,
              private toastr: ToastrService,
              private translateService: TranslateService) {
  }

  async addUser(user: User): Promise<boolean> {
    return await this.registerUser(user);
  }

  async getSessionToken(user: User, cookieService: CookieService): Promise<boolean> {
    return await this.loginUser(user, cookieService);
  }

  async updateUserInfo(updateDto: UserUpdateDto): Promise<boolean> {
    return await this.updateUser(updateDto);
  }

  private updateUser(updateDto: UserUpdateDto): Promise<boolean> {
    return this.http.put(EndpointHolder.userEndpoint, updateDto, {responseType: 'text'}).toPromise().then(response => {
      this.toastr.success(this.getSuccessUpdateTranslation());
      return true;
    })
      .catch((error: any) => {
        this.toastr.error(this.getFailureUpdateTranslation())
        return false;
      });
  }

  private registerUser(user: User): Promise<boolean> {
    return this.http.post(EndpointHolder.userEndpoint, user, {responseType: 'text'})
      .toPromise()
      .then(response => {
        this.toastr.success(this.getSuccessRegisterTranslation(user.username));
        return true;
      })
      .catch(() => {
        this.toastr.error(this.getFailureRegisterTranslation());
        return false;
      })
  }

  private loginUser(user: User, cookieService: CookieService): Promise<boolean> {
    return this.http.post<JSON>(EndpointHolder.loginEndpoint,
      {
        "username": user.username,
        "password": user.password
      },
      {responseType: 'json'})
      .toPromise()
      .then(response => {
        cookieService.set("username", user.username);
        cookieService.set("sessionToken", response['data']);
        this.toastr.success(this.getSuccessLoginTranslation());
        return true;
      })
      .catch(() => {
        this.toastr.error(this.getFailureLoginTranslation());
        return false;
      });
  }

  private getSuccessLoginTranslation(){
    return this.translateService.instant("DialogLogin_Success");
  }

  private getFailureLoginTranslation(){
    return this.translateService.instant("DialogLogin_Failure");
  }

  private getSuccessRegisterTranslation(username: String){
    return this.translateService.instant("DialogRegister_Success1") + username + this.translateService.instant("DialogRegister_Success2");
  }

  private getFailureRegisterTranslation(){
    return this.translateService.instant("DialogRegister_Failure");
  }

  private getSuccessUpdateTranslation(){
    return this.translateService.instant("DialogUpdate_Success");
  }

  private getFailureUpdateTranslation(){
    return this.translateService.instant("DialogUpdate_Failure");
  }

}
