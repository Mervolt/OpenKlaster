import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstallationViewComponent } from './installation-view.component';

describe('InstallationViewComponent', () => {
  let component: InstallationViewComponent;
  let fixture: ComponentFixture<InstallationViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InstallationViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstallationViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
