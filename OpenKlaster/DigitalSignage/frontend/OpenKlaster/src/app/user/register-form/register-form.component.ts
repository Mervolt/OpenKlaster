import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

import {RegisterFormService} from '../register-form.service';
import {User} from '../../model/User';
import {AppComponent} from "../../app.component";
import {MatDialog} from "@angular/material/dialog";
import {SuccessfulLoginDialogComponent} from "../successful-login-dialog/successful-login-dialog.component";


@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.css']
})
export class RegisterFormComponent implements OnInit {
  model = new User('', '', '');

  constructor(public service: RegisterFormService, public appComp: AppComponent, private router: Router, public dialog: MatDialog) {
    this.appComp.refreshBackground(0)
  }

  ngOnInit(): void {
  }

  async onSubmit() {
    let success = await this.service.addUser(this.model);
    if (success) {
      let dialog = this.dialog.open(SuccessfulLoginDialogComponent, {
        width: '500px'
      })

      dialog.componentInstance.user = this.model.username

      this.router.navigate(['/login']).then();
    }
  }
}
