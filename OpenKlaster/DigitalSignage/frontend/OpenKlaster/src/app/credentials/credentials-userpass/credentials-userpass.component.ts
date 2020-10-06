import {Component, OnInit, Output, EventEmitter} from '@angular/core';

@Component({
  selector: 'app-credentials-userpass',
  templateUrl: './credentials-userpass.component.html',
  styleUrls: ['./credentials-userpass.component.css']
})
export class CredentialsUserpassComponent implements OnInit {
  @Output() userpassEmitter = new EventEmitter<JSON>()
  username: string
  password: string

  constructor() {
  }

  ngOnInit(): void {
  }

  sendMessage() {
    let jsonObject: any = {
      "username": this.username,
      "password": this.password
    }
    this.userpassEmitter.emit(<JSON>jsonObject)
  }
}
