import { Injectable } from '@angular/core';
import {User} from './model/User';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LoginFormService {

  constructor(public http: HttpClient) { }

  getToken(model: User): boolean {
    this.http.post<User>("http://localhost:8082/api/1/user/login",
      {
        'username' : model.username,
        'password' : model.password
      })
      .pipe()
    return true;
  }
}
