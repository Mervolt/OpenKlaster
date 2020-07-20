import { TestBed } from '@angular/core/testing';

import { TokenPanelService } from './token-panel.service';

describe('TokenPanelService', () => {
  let service: TokenPanelService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TokenPanelService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
