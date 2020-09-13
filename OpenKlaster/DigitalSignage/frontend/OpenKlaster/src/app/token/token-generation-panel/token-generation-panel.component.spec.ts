import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TokenGenerationPanelComponent } from './token-generation-panel.component';

describe('TokenGenerationPanelComponent', () => {
  let component: TokenGenerationPanelComponent;
  let fixture: ComponentFixture<TokenGenerationPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TokenGenerationPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TokenGenerationPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
