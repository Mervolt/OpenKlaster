import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CookieService} from 'ngx-cookie-service';
import {CookieHolder} from "../model/CookieHolder";
import {EndpointHolder} from "../model/EndpointHolder";

@Injectable({
  providedIn: 'root'
})
//TODO why `Panel`?
//MM-ANSWER: Z angielskiego pulpit :)
export class TokenPanelService {

  constructor(public http: HttpClient) {
  }

  getTokens(cookies: CookieService): Observable<any> {
    let params = new HttpParams()
      //TODO Isn't it possible in TS to prepare some static property - if some key is changed then we have only one place to change
      //MM-ANSWER done
      .set('username', cookies.get(CookieHolder.usernameKey))
      .set('sessionToken', cookies.get(CookieHolder.tokenKey));

    //TODO hardcoded
    //MM:ANSWER done
    return this.http.get(EndpointHolder.userEndpoint, {params: params})
  }

  //TODO you are generating, not adding - you don't provide its data
  //MM-ANSWER:done
  generateToken(cookies: CookieService): Observable<any> {
    let params = new HttpParams().set('sessionToken', cookies.get(CookieHolder.tokenKey));
    //TODO hardcoded
    return this.http.post(EndpointHolder.tokenEndpoint,
      {'username': cookies.get('username')}, {params: params});
  }
}
