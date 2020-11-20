import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DigitalSignageComponent } from './digital-signage.component';

describe('DigitalSignageComponent', () => {
  let component: DigitalSignageComponent;
  let fixture: ComponentFixture<DigitalSignageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DigitalSignageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DigitalSignageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
