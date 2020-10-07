import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CredentialsTokenComponent } from './credentials-token.component';

describe('CredentialsTokenComponent', () => {
  let component: CredentialsTokenComponent;
  let fixture: ComponentFixture<CredentialsTokenComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CredentialsTokenComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CredentialsTokenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
