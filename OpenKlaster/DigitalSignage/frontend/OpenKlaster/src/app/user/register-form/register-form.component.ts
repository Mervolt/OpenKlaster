import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

import { RegisterFormService } from '../register-form.service';
import { User } from '../../model/User';


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

  async onSubmit(){
    let success = await this.service.addUser(this.model);
    if(success)
      this.router.navigate(['/login']).then();
  }

  redirectToLogin() {
    //TODO ditto navigation service
    //MM-ANSWER: Look navbar.component.ts
    this.router.navigate(['login']);
  }
}
