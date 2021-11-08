import {Component, OnInit} from '@angular/core';
import {User} from '../../model/User';
import {Router} from '@angular/router';
import {AppComponent} from '../../app.component';
import {UserService} from '../../service/user.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {
  model = new User('', '', '');

  constructor(public service: UserService, public appComp: AppComponent, private router: Router) {
    this.appComp.refreshBackground(0);
  }

  ngOnInit(): void {
  }

  async onSubmit() {
    const success = await this.service.getSessionToken(this.model, this.appComp.cookieService);
    if (success) {
      this.router.navigate(['/installations']).then();
    }
  }

  redirectToRegister() {
    this.router.navigate(['/register']);
  }
}
