import { Injectable } from '@angular/core';
import {User} from '../model/User';
import {HttpClient} from '@angular/common/http';
import {CookieService} from "ngx-cookie-service";
import {EndpointHolder} from "../model/EndpointHolder";

@Injectable({
  providedIn: 'root'
})
export class LoginFormService {
  errorReasonKey: string = 'error'
  constructor(public http: HttpClient) { }

  //TODO which token? session or api?
  //: )
  async getSessionToken(user: User, cookieService: CookieService): Promise<boolean> {
    return await this.postUser(user, cookieService);
  }

  postUser(user: User, cookieService: CookieService): Promise<boolean> {
    //TODO hardcoded?
    return this.http.post<JSON>(EndpointHolder.loginEndpoint,
      {
        "username": user.username,
        "password": user.password
      },
      {responseType: 'json'})
      .toPromise()
      .then(response => {
        cookieService.set("username", user.username);
        //TODO Should you not store expirationDate somehow? To know when token is expired and e.g. logout user on action?
        //MM-ANSWER I will timeout when trying to send with expired token
        cookieService.set("sessionToken", response['sessionToken']['data']);
        return true;
      })
      .catch((error:any) => {
        //TODO maybe log on which method/service failed
        //MM-ANSWER: Improved but I dont have info which method/service
        alert(error[this.errorReasonKey]);//TODO
        return false;
      })
  }
}
