import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleTokenPanelComponent } from './single-token-panel.component';

describe('SingleTokenPanelComponent', () => {
  let component: SingleTokenPanelComponent;
  let fixture: ComponentFixture<SingleTokenPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SingleTokenPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SingleTokenPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
