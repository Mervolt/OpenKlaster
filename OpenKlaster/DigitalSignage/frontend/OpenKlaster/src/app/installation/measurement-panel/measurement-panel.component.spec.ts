import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MeasurementPanelComponent } from './measurement-panel.component';

describe('MeasurementPanelComponent', () => {
  let component: MeasurementPanelComponent;
  let fixture: ComponentFixture<MeasurementPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MeasurementPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MeasurementPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
