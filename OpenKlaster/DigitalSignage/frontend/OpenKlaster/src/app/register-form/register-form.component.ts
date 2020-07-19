import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

import { RegisterFormService } from '../register-form.service';
import { User } from '../model/User';


@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.css']
})
export class RegisterFormComponent implements OnInit {
  model = new User('', '', '');

  constructor(public service: RegisterFormService, private router: Router) {

  }

  ngOnInit(): void {
  }

  onSubmit(){
    let success = this.service.addUser(this.model);
    if(success)
      this.router.navigate(['/user']);
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }
}
