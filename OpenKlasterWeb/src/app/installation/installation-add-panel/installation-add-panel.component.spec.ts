import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstallationAddPanelComponent } from './installation-add-panel.component';

// TODO Even angular says : "Hey, test me man!"
// MM-ANSWER: In future
describe('InstallationGenerationPanelComponent', () => {
  let component: InstallationAddPanelComponent;
  let fixture: ComponentFixture<InstallationAddPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InstallationAddPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstallationAddPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
