import { TestBed } from '@angular/core/testing';

import { ManufacturerCredentialService } from './manufacturer-credential.service';

describe('ManufacturerCredentialService', () => {
  let service: ManufacturerCredentialService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ManufacturerCredentialService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
