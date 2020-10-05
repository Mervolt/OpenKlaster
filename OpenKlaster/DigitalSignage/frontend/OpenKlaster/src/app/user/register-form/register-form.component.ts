import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

import {RegisterFormService} from '../register-form.service';
import {User} from '../../model/User';
import {AppComponent} from "../../app.component";


@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.css']
})
export class RegisterFormComponent implements OnInit {
  model = new User('', '', '');

  constructor(public service: RegisterFormService, public appComp: AppComponent, private router: Router) {
    this.appComp.refreshBackground(0)
  }

  ngOnInit(): void {
  }

  async onSubmit() {
    let success = await this.service.addUser(this.model);
    if (success)
      this.router.navigate(['/login']).then();
  }

  redirectToLogin() {
    //TODO ditto navigation service
    //MM-ANSWER: Look navbar.component.ts
    this.router.navigate(['login']);
  }
}
