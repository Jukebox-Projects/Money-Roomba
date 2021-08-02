import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WalletSliderComponent } from './wallet-slider.component';

describe('WalletSliderComponent', () => {
  let component: WalletSliderComponent;
  let fixture: ComponentFixture<WalletSliderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WalletSliderComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WalletSliderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
