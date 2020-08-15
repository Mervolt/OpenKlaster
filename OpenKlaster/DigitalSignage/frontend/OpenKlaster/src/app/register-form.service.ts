import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from './model/User';

@Injectable({
  providedIn: 'root'
})
export class RegisterFormService {

  constructor(public http: HttpClient) {
  }

  async addUser(user: User): Promise<boolean> {
    return await this.postUser(user);
  }

  postUser(user: User):Promise<boolean> {
    return this.http.post("http://localhost:8082/api/1/user", user, {responseType: 'text'})
      .toPromise()
      .then(response => {
        return true;
      })
      .catch((_:any) => {
        return false;
      })
  }

}
