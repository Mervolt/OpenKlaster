import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CookieService} from 'ngx-cookie-service';
import {CookieHolder} from "../../model/CookieHolder";
import {EndpointHolder} from "../../model/EndpointHolder";

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor(public http: HttpClient) {
  }

  getTokens(cookies: CookieService): Observable<any> {
    let params = new HttpParams()
      .set('username', cookies.get(CookieHolder.usernameKey))
      .set('sessionToken', cookies.get(CookieHolder.tokenKey));

    return this.http.get(EndpointHolder.userEndpoint, {params: params})
  }

  generateToken(cookies: CookieService): Observable<any> {
    let params = new HttpParams().set('sessionToken', cookies.get(CookieHolder.tokenKey));
    return this.http.post(EndpointHolder.tokenEndpoint,
      {'username': cookies.get('username')}, {params: params});
  }

  deleteToken(cookies: CookieService, token: string): Observable<any> {
    let params = new HttpParams().set('apiToken', token).set('username', cookies.get(CookieHolder.usernameKey));
    return this.http.delete(EndpointHolder.tokenEndpoint, {params: params})
  }

  deleteAllTokens(cookies: CookieService): Observable<any> {
    let params = new HttpParams()
      .set('sessionToken', cookies.get(CookieHolder.tokenKey))
      .set('username', cookies.get(CookieHolder.usernameKey));
    return this.http.delete(EndpointHolder.allTokensEndpoint,{params: params});
  }
}
