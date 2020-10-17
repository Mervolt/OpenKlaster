import { TestBed } from '@angular/core/testing';

import { InstallationService } from './installation.service';

describe('InstallationPanelService', () => {
  let service: InstallationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InstallationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
