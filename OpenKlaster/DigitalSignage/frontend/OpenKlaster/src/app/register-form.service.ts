import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {User} from './model/User';
import {catchError, tap} from 'rxjs/operators';
import {throwError} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegisterFormService {

  constructor(public http: HttpClient) {
  }

  addUser(user: User): boolean {
    let params = new HttpParams();
    this.http.post<User>("localhost:8082/api/1/user", user,{params : params})
      .pipe(
        catchError(this.handleError)
      )
    return true;
  }


  private handleError() {
    return throwError(
      'Something bad happened; please try again later.');
  }
}
