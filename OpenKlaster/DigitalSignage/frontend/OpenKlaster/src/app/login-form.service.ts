import { Injectable } from '@angular/core';
import {User} from './model/User';
import {HttpClient} from '@angular/common/http';
import {CookieService} from "ngx-cookie-service";

@Injectable({
  providedIn: 'root'
})
export class LoginFormService {

  constructor(public http: HttpClient) { }

  async getToken(user: User, cookieService: CookieService): Promise<boolean> {
    return await this.postUser(user, cookieService);
  }

  postUser(user: User, cookieService: CookieService): Promise<boolean> {
    return this.http.post<JSON>("http://localhost:8082/api/1/user/login",
      {
        "username": user.username,
        "password": user.password
      },
      {responseType: 'json'})
      .toPromise()
      .then(response => {
        console.log(response);
        cookieService.set("username", user.username);
        cookieService.set("sessionToken", response['sessionToken']['data']);
        return true;
      })
      .catch((error:any) => {
        console.log(error);
        return false;
      })
  }
}
