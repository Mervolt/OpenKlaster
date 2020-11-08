import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DescriptionTileComponent } from './description-tile.component';

describe('DescriptionTileComponent', () => {
  let component: DescriptionTileComponent;
  let fixture: ComponentFixture<DescriptionTileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DescriptionTileComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DescriptionTileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
