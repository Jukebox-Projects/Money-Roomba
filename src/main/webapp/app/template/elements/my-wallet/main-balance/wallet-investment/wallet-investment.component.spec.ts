import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WalletInvestmentComponent } from './wallet-investment.component';

describe('WalletInvestmentComponent', () => {
  let component: WalletInvestmentComponent;
  let fixture: ComponentFixture<WalletInvestmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WalletInvestmentComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WalletInvestmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
