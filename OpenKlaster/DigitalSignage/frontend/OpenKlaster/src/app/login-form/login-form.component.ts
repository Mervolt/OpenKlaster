import { Component, OnInit } from '@angular/core';
import {User} from '../model/User';
import {LoginFormService} from '../login-form.service';
import {Router} from '@angular/router';
import {CookieService} from 'ngx-cookie-service';
import {AppComponent} from '../app.component';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {
  model = new User('', '', '');

  constructor(public service: LoginFormService, public appComp: AppComponent, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit() {
    let success = this.service.getToken(this.model);
    this.appComp.cookieService.set('username', this.model.username);
    if(success)
      this.router.navigate(['/user']);
  }

  redirectToRegister() {
    this.router.navigate(['']);
  }
}
