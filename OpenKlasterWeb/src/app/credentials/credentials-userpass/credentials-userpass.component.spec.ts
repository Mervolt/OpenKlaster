import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CredentialsUserpassComponent } from './credentials-userpass.component';

describe('CredentialsUserpassComponent', () => {
  let component: CredentialsUserpassComponent;
  let fixture: ComponentFixture<CredentialsUserpassComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CredentialsUserpassComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CredentialsUserpassComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
