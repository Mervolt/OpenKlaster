import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestResponseComponentComponent } from './request-response-component.component';

describe('RequestResponseComponentComponent', () => {
  let component: RequestResponseComponentComponent;
  let fixture: ComponentFixture<RequestResponseComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RequestResponseComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RequestResponseComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
