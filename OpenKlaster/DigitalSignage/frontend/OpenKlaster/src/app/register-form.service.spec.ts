import { TestBed } from '@angular/core/testing';

import { RegisterFormService } from './register-form.service';

describe('RegisterFormService', () => {
  let service: RegisterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RegisterFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
