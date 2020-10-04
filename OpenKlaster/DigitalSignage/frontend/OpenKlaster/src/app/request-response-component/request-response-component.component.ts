import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-request-response-component',
  templateUrl: './request-response-component.component.html',
  styleUrls: ['./request-response-component.component.css']
})
export class RequestResponseComponentComponent implements OnInit {
  @Input() sendRequestState: string
  @Input() requestReceivedState: string
  constructor() { }

  ngOnInit(): void {
  }

}
