import { Injectable } from '@angular/core';
import {User} from './model/User';
import {HttpClient} from '@angular/common/http';
import {CookieService} from "ngx-cookie-service";

@Injectable({
  providedIn: 'root'
})
export class LoginFormService {

  constructor(public http: HttpClient) { }

  //TODO which token? session or api?
  async getToken(user: User, cookieService: CookieService): Promise<boolean> {
    return await this.postUser(user, cookieService);
  }

  postUser(user: User, cookieService: CookieService): Promise<boolean> {
    //TODO hardcoded?
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
        //TODO Should you not store expirationDate somehow? To know when token is expired and e.g. logout user on action?
        cookieService.set("sessionToken", response['sessionToken']['data']);
        return true;
      })
      .catch((error:any) => {
        //TODO maybe log on which method/service failed
        console.log(error);
        return false;
      })
  }
}
