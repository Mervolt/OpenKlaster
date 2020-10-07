import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstallationListItemComponent } from './installation-list-item.component';

describe('InstallationListItemComponent', () => {
  let component: InstallationListItemComponent;
  let fixture: ComponentFixture<InstallationListItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InstallationListItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstallationListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
