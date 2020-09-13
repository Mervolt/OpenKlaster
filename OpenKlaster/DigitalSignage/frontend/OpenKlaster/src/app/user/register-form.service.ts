import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../model/User';
import {EndpointHolder} from "../model/EndpointHolder";

@Injectable({
  providedIn: 'root'
})
export class RegisterFormService {
  errorReasonKey: string = 'error'
  constructor(public http: HttpClient) {
  }

  async addUser(user: User): Promise<boolean> {
    return await this.postUser(user);
  }

  postUser(user: User):Promise<boolean> {
    //TODO hardcoded
    return this.http.post(EndpointHolder.userEndpoint, user, {responseType: 'text'})
      .toPromise()
      .then(response => {
        return true;
      })
      .catch((error :any) => {
        alert(error[this.errorReasonKey]);
        return false;
      })
  }

}
