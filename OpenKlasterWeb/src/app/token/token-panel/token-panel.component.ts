import {Component, OnInit} from '@angular/core';
import {TokenService} from '../../service/token/token.service';
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
  requestState = 'none'
  displayedColumns: string[] = ['number', 'token', 'removeButton']
  loading: boolean = false;

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

  myCallbackFunction = (): void => {
    this.addToken()
  }

  addToken() {
    this.requestState = 'waiting'
    this.loading = true
    let addPromise = this.tokenPanelService.generateToken(this.cookieService).toPromise();
    this.resolvePromise(addPromise)
  }

  removeAllTokens() {
    if(!window.confirm('Are sure you want to delete all tokens?')) {
      return;
    }
    this.requestState = 'waiting'
    this.loading = true
    let removeAllPromise = this.tokenPanelService.deleteAllTokens(this.cookieService).toPromise();
    this.resolvePromise(removeAllPromise)
    this.loading = false
  }

  removeToken(token: string) {
    if(!window.confirm('Are sure you want to delete this token?')) {
      return;
    }
    this.requestState = 'waiting'
    let removePromise = this.tokenPanelService.deleteToken(this.cookieService, token).toPromise();
    this.resolvePromise(removePromise)
  }

  private resolvePromise(promise: Promise<any>) {
    promise
      .then(() => {
        this.requestState = 'success'
        this.ngOnInit()
      })
      .catch(() => {
        this.requestState = 'failure'
        this.ngOnInit()
      })
    setTimeout(() => {
      this.requestState = 'none'
    }, 3500);
  }
}
