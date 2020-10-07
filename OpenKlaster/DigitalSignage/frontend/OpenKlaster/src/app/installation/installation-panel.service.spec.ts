import { TestBed } from '@angular/core/testing';

import { InstallationPanelService } from './installation-panel.service';

describe('InstallationPanelService', () => {
  let service: InstallationPanelService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InstallationPanelService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
