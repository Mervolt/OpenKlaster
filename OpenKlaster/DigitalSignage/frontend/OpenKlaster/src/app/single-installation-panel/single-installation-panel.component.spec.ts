import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleInstallationPanelComponent } from './single-installation-panel.component';

describe('SingleInstallationPanelComponent', () => {
  let component: SingleInstallationPanelComponent;
  let fixture: ComponentFixture<SingleInstallationPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SingleInstallationPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SingleInstallationPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
