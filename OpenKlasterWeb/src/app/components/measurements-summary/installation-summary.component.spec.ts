import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstallationSummaryComponent } from './installation-summary.component';

describe('MeasurementsSummaryComponent', () => {
  let component: InstallationSummaryComponent;
  let fixture: ComponentFixture<InstallationSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InstallationSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstallationSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
