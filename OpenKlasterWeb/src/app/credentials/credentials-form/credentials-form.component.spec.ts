import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CredentialsFormComponent } from './credentials-form.component';

describe('CredentialsFormComponent', () => {
  let component: CredentialsFormComponent;
  let fixture: ComponentFixture<CredentialsFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CredentialsFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CredentialsFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
