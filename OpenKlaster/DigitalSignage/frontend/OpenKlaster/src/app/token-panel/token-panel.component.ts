import { Component, OnInit } from '@angular/core';
import { TokenPanelService } from '../token-panel.service';

@Component({
  selector: 'app-token-panel',
  templateUrl: './token-panel.component.html',
  styleUrls: ['./token-panel.component.css']
})
export class TokenPanelComponent implements OnInit {
  tokens;

  constructor(service: TokenPanelService) {
    this.tokens = service.getTokens()
    this.tokens.subscribe(response =>{
      return response.body.tokens;
    })
  }

  ngOnInit(): void {
  }

}
