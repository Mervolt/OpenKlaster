import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';

@Component({
  selector: 'app-credentials-form',
  templateUrl: './credentials-form.component.html',
  styleUrls: ['./credentials-form.component.css']
})
export class CredentialsFormComponent implements OnInit {
  @Input() credentialsType: string
  @Output() credentialsEventEmitter = new EventEmitter<JSON>()
  credentials
  token: string
  username: string
  password: string

  constructor() {
  }

  ngOnInit(): void {
  }

  sendMessage($event) {
    // let jsonObject: any
    // if (this.credentialsType == 'userpass') {
    //   jsonObject = {
    //     "username": this.username,
    //     "password": this.password
    //   }
    // } else {
    //   jsonObject = {
    //     "token": this.token
    //   }
    // }
    // // Looks ugly as hell but they do it like this on stack, comment for you to know..
    // this.credentials = <JSON>jsonObject
    this.credentialsEventEmitter.emit($event)
  }

  receiveMessage($event) {
    console.log($event)
    this.sendMessage($event)
  }
}
