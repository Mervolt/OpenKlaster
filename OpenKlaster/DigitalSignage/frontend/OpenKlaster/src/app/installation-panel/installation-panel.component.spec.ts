import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstallationPanelComponent } from './installation-panel.component';

describe('InstallationPanelComponent', () => {
  let component: InstallationPanelComponent;
  let fixture: ComponentFixture<InstallationPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InstallationPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstallationPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
