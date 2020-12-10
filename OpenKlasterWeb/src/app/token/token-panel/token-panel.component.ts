import {Component, OnInit} from '@angular/core';
import {TokenService} from '../../service/token/token.service';
import {AppComponent} from '../../app.component';
import {MatDialog} from "@angular/material/dialog";
import {DeleteTokenDialogComponent} from "../delete-token-dialog/delete-token-dialog.component";

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

  constructor(public tokenPanelService: TokenService, public appComp: AppComponent, private dialog: MatDialog) {
    this.cookieService = appComp.cookieService;
  }

  ngOnInit(): void {
    let request = this.tokenPanelService.getUserInfo(this.appComp.cookieService);
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

  openDeleteTokenConfirmationWindow(token: String){
    let dialog = this.dialog.open(DeleteTokenDialogComponent, {
      width: '500px'
    })
    dialog.componentInstance.allTokens = false;
    dialog.componentInstance.token = token
    dialog.componentInstance.parentComp = this
  }

  openDeleteAllTokensConfirmationWindow(){
    let dialog = this.dialog.open(DeleteTokenDialogComponent, {
      width: '500px'
    })
    dialog.componentInstance.allTokens = true;
    dialog.componentInstance.parentComp = this
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
