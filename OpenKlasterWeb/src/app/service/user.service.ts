import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../model/User';
import {EndpointHolder} from "../model/EndpointHolder";
import {CookieService} from "ngx-cookie-service";
import {UserUpdateDto} from "../model/UserUpdateDto";
import {ToastrService} from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  errorReasonKey: string = 'error'

  constructor(public http: HttpClient,
              private toastr: ToastrService) {
  }

  async addUser(user: User): Promise<boolean> {
    return await this.registerUser(user);
  }

  async getSessionToken(user: User, cookieService: CookieService): Promise<boolean> {
    return await this.loginUser(user, cookieService);
  }

  async updateUserInfo(updateDto: UserUpdateDto): Promise<boolean> {
    return await this.updateUser(updateDto);
  }

  private updateUser(updateDto: UserUpdateDto): Promise<boolean> {
    return this.http.put(EndpointHolder.userEndpoint, updateDto).toPromise().then(response => {
      this.toastr.success('Pomyślnie zaktualizowane twoje dane');
      return true;
    })
      .catch((error: any) => {
        this.toastr.error('Wystąpił problem z aktualizowaniem twoich danych.')
        return false;
      });
  }

  private registerUser(user: User): Promise<boolean> {
    return this.http.post(EndpointHolder.userEndpoint, user, {responseType: 'text'})
      .toPromise()
      .then(response => {
        this.toastr.success('Konto użytkownika ' + user.username + ' zostało utworzone pomyślnie!');
        return true;
      })
      .catch(() => {
        this.toastr.error('Wystąpił problem z utworzeniem użytkownika. Spróbuj użyć innej nazwy.');
        return false;
      })
  }

  private loginUser(user: User, cookieService: CookieService): Promise<boolean> {
    return this.http.post<JSON>(EndpointHolder.loginEndpoint,
      {
        "username": user.username,
        "password": user.password
      },
      {responseType: 'json'})
      .toPromise()
      .then(response => {
        cookieService.set("username", user.username);
        cookieService.set("sessionToken", response['sessionToken']['data']);
        this.toastr.success('Pomyślnie zalogowano');
        return true;
      })
      .catch(() => {
        this.toastr.error('Następił problem z logowaniem. Sprawdź swoją nazwę użydkownika oraz hasło.');
        return false;
      })
  }


}
