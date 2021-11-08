import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-credentials-token',
  templateUrl: './credentials-token.component.html',
  styleUrls: ['./credentials-token.component.css']
})
export class CredentialsTokenComponent implements OnInit {
  @Output() tokenEmitter = new EventEmitter<JSON>();
  token: string;

  constructor() {

  }

  ngOnInit(): void {
  }

  sendMessage() {
    const jsonObject: any = {
      token: this.token
    };
    this.tokenEmitter.emit(jsonObject as JSON);
  }
}
