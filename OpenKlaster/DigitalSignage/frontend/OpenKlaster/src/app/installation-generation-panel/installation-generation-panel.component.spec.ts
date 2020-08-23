import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstallationGenerationPanelComponent } from './installation-generation-panel.component';

describe('InstallationGenerationPanelComponent', () => {
  let component: InstallationGenerationPanelComponent;
  let fixture: ComponentFixture<InstallationGenerationPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InstallationGenerationPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstallationGenerationPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
