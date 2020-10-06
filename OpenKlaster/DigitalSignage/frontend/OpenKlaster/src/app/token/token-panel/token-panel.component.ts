import {Component, OnInit} from '@angular/core';
import {TokenService} from '../token.service';
import {AppComponent} from '../../app.component';

export interface TokenResponse {
  data: string
}

export interface TabledToken {
  number: number;
  token: string;
}

@Component({
  selector: 'app-token-panel',
  templateUrl: './token-panel.component.html',
  styleUrls: ['./token-panel.component.css']
})
export class TokenPanelComponent implements OnInit {
  tokens: any
  tabledTokens: { number: number, token: string }[] = []
  cookieService
  requestReceivedState = 'wait'
  sendRequestState = 'wait'
  submittedObjectName = 'Token'
  displayedColumns: string[] = ['number', 'token']
  //TODO change `service` variable name
  //MM-ANSWER Done
  constructor(public tokenPanelService: TokenService, public appComp: AppComponent) {
    this.cookieService = appComp.cookieService;
  }

  ngOnInit(): void {
    let request = this.tokenPanelService.getTokens(this.appComp.cookieService);
    request.subscribe(response => {
      this.tabledTokens = []
      let tokensData: TokenResponse[] = <TokenResponse[]>(response["userTokens"])
      let counter = 0
      tokensData.map(element => {
        this.tabledTokens.push({number: counter, token: element.data})
        counter++
      })
      this.tokens = response["userTokens"]
    })
  }

  addToken() {
    this.sendRequestState = 'sent'
    let addPromise = this.tokenPanelService.generateToken(this.cookieService).toPromise();
    addPromise
      .then(() => {
        this.sendRequestState = 'received'
        this.requestReceivedState = 'success'
        this.ngOnInit()
      })
      .catch(() => {
        this.sendRequestState = 'received'
        this.requestReceivedState = 'fail'
        this.ngOnInit()
      })
  }
}
