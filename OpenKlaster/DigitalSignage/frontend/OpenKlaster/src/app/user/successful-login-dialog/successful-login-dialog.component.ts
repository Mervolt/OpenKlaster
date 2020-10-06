import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-successful-login-dialog',
  templateUrl: './successful-login-dialog.component.html',
  styleUrls: ['./successful-login-dialog.component.css']
})
export class SuccessfulLoginDialogComponent implements OnInit {
  user: string;

  constructor() { }

  ngOnInit(): void {
  }

}
