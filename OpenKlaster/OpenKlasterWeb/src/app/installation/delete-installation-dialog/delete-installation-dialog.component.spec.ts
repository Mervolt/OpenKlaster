import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteInstallationDialogComponent } from './delete-installation-dialog.component';

describe('DeleteInstallationDialogComponent', () => {
  let component: DeleteInstallationDialogComponent;
  let fixture: ComponentFixture<DeleteInstallationDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeleteInstallationDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteInstallationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
