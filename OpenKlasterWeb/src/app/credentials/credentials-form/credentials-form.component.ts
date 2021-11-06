import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';

@Component({
  selector: 'app-credentials-form',
  templateUrl: './credentials-form.component.html',
  styleUrls: ['./credentials-form.component.css']
})
export class CredentialsFormComponent implements OnInit {
  @Input() credentialsType: string;
  @Output() credentialsEventEmitter = new EventEmitter<JSON>();
  credentials;

  constructor() {
  }

  ngOnInit(): void {
  }

  sendMessage($event) {
    this.credentialsEventEmitter.emit($event);
  }

  receiveMessage($event) {
    this.sendMessage($event);
  }
}
