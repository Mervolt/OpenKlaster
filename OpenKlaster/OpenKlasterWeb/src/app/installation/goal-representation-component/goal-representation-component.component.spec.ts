import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GoalRepresentationComponentComponent } from './goal-representation-component.component';

describe('GoalRepresentationComponentComponent', () => {
  let component: GoalRepresentationComponentComponent;
  let fixture: ComponentFixture<GoalRepresentationComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GoalRepresentationComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GoalRepresentationComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
