import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-delete-token-dialog',
  templateUrl: './delete-token-dialog.component.html',
  styleUrls: ['./delete-token-dialog.component.css']
})
export class DeleteTokenDialogComponent implements OnInit {
  token: String
  allTokens: boolean
  parentComp

  constructor() { }

  ngOnInit(): void {
  }

  deleteAllTokens() {
    this.parentComp.requestState = 'waiting'
    this.parentComp.loading = true
    let removeAllPromise = this.parentComp.tokenPanelService.deleteAllTokens(this.parentComp.cookieService).toPromise();
    this.parentComp.resolvePromise(removeAllPromise)
    this.parentComp.loading = false
  }

  deleteToken(token: String) {
    this.parentComp.requestState = 'waiting'
    let removePromise = this.parentComp.tokenPanelService.deleteToken(this.parentComp.cookieService, token).toPromise();
    this.parentComp.resolvePromise(removePromise)
  }
}
