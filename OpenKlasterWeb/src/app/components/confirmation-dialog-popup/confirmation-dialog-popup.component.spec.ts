import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmationDialogPopupComponent } from './confirmation-dialog-popup.component';

describe('SuccessfulLoginDialogComponent', () => {
  let component: ConfirmationDialogPopupComponent;
  let fixture: ComponentFixture<ConfirmationDialogPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfirmationDialogPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmationDialogPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
