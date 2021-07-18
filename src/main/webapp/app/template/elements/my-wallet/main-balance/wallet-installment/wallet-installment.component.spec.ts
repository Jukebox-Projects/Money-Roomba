import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WalletInstallmentComponent } from './wallet-installment.component';

describe('WalletInstallmentComponent', () => {
  let component: WalletInstallmentComponent;
  let fixture: ComponentFixture<WalletInstallmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WalletInstallmentComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WalletInstallmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
