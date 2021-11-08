import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

import {UserService} from '../../service/user.service';
import {User} from '../../model/User';
import {AppComponent} from '../../app.component';
import {MatDialog} from '@angular/material/dialog';
import {ConfirmationDialogPopupComponent} from '../../components/confirmation-dialog-popup/confirmation-dialog-popup.component';


@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.css']
})
export class RegisterFormComponent implements OnInit {
  model = new User('', '', '');

  constructor(public service: UserService, public appComp: AppComponent, private router: Router, public dialog: MatDialog) {
    this.appComp.refreshBackground(0);
  }

  ngOnInit(): void {
  }

  async onSubmit() {
    const success = await this.service.addUser(this.model);
    if (success) {
      this.router.navigate(['/login']).then();
    }
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }
}
