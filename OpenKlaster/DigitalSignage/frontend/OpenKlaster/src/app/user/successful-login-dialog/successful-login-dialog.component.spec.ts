import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SuccessfulLoginDialogComponent } from './successful-login-dialog.component';

describe('SuccessfulLoginDialogComponent', () => {
  let component: SuccessfulLoginDialogComponent;
  let fixture: ComponentFixture<SuccessfulLoginDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SuccessfulLoginDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SuccessfulLoginDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
