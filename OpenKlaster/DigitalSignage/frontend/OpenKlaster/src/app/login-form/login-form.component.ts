import { Component, OnInit } from '@angular/core';
import {User} from '../model/User';
import {LoginFormService} from '../login-form.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {
  model = new User('', '', '');

  constructor(public service: LoginFormService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit() {
    let success = this.service.getToken(this.model);
    if(success)
      this.router.navigate(['/user']);
  }

  redirectToRegister() {
    this.router.navigate(['']);
  }
}
