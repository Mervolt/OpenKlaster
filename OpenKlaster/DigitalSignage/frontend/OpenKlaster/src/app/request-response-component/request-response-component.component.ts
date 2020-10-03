import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-request-response-component',
  templateUrl: './request-response-component.component.html',
  styleUrls: ['./request-response-component.component.css']
})
export class RequestResponseComponentComponent implements OnInit {
  sendRequestState: string = 'wait'
  requestReceivedState: string = 'wait'
  constructor() { }

  ngOnInit(): void {
  }

}
