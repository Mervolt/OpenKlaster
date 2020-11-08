import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-successful-login-dialog',
  templateUrl: './confirmation-dialog-popup.component.html',
  styleUrls: ['./confirmation-dialog-popup.component.css']
})
export class ConfirmationDialogPopupComponent implements OnInit {
  popupContent: string;

  constructor() { }

  ngOnInit(): void {
  }

}
