import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstallationDetailsBoxComponent } from './installation-details-box.component';

describe('InstallationDetailsBoxComponent', () => {
  let component: InstallationDetailsBoxComponent;
  let fixture: ComponentFixture<InstallationDetailsBoxComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InstallationDetailsBoxComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstallationDetailsBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
