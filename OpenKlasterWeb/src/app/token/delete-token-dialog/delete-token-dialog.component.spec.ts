import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteTokenDialogComponent } from './delete-token-dialog.component';

describe('DeleteTokenDialogComponent', () => {
  let component: DeleteTokenDialogComponent;
  let fixture: ComponentFixture<DeleteTokenDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeleteTokenDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteTokenDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
