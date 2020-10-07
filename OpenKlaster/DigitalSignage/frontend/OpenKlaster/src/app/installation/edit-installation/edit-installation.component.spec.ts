import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditInstallationComponent } from './edit-installation.component';

describe('EditInstallationComponent', () => {
  let component: EditInstallationComponent;
  let fixture: ComponentFixture<EditInstallationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditInstallationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditInstallationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
