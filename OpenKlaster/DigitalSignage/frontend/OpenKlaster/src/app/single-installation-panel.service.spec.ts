import { TestBed } from '@angular/core/testing';

import { SingleInstallationPanelService } from './single-installation-panel.service';

describe('SingleInstallationPanelService', () => {
  let service: SingleInstallationPanelService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SingleInstallationPanelService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
