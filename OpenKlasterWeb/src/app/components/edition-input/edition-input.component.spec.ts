import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditionInputComponent } from './edition-input.component';

describe('EditionInputComponent', () => {
  let component: EditionInputComponent;
  let fixture: ComponentFixture<EditionInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditionInputComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditionInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
