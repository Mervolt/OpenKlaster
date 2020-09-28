import {Component, OnInit} from '@angular/core';
import {User} from '../../model/User';
import {LoginFormService} from '../login-form.service';
import {Router} from '@angular/router';
//TODO unused import
import {AppComponent} from '../../app.component';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {
  model = new User('', '', '');

  constructor(public service: LoginFormService, public appComp: AppComponent, private router: Router) {
  }

  ngOnInit(): void {
  }

  async onSubmit() {
    let success = await this.service.getToken(this.model, this.appComp.cookieService);

    if (success)
      this.router.navigate(['/user']).then()
  }

  redirectToRegister() {
    // TODO commands are empty because register is root of the page?
    //MM-ANSWER: For now register is set to be '' so we have register page when we enter our website
    //e.g localhost:4200 not localhost:4200/register
    this.router.navigate(['']);
  }
}
