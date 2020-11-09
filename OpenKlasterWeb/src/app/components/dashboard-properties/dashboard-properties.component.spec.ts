import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardPropertiesComponent } from './dashboard-properties.component';

describe('DashboardPropertiesComponent', () => {
  let component: DashboardPropertiesComponent;
  let fixture: ComponentFixture<DashboardPropertiesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardPropertiesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardPropertiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
