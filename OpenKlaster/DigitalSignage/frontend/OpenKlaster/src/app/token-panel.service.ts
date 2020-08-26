import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import {CookieService} from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
//TODO why `Panel`?
export class TokenPanelService {

  constructor(public http: HttpClient) { }

  getTokens(cookies: CookieService): Observable<any>{
    let params = new HttpParams()
      //TODO Isn't it possible in TS to prepare some static property - if some key is changed then we have only one place to change
      .set('username', cookies.get('username'))
      .set('sessionToken', cookies.get('sessionToken'));

    //TODO hardcoded
    return this.http.get("http://localhost:8082/api/1/user",{params : params})
  }

  //TODO you are generating, not adding - you don't provide its data
  addToken(cookies: CookieService): Observable<any>{
    let params = new HttpParams().set('sessionToken', cookies.get('sessionToken'));
    //TODO hardcoded
    return this.http.post("http://localhost:8082/api/1/token",
      {'username' : cookies.get('username')},{params : params});
  }
}
