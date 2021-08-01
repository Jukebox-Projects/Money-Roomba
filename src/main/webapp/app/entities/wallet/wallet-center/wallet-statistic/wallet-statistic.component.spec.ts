import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WalletStatisticComponent } from './wallet-statistic.component';

describe('WalletStatisticComponent', () => {
  let component: WalletStatisticComponent;
  let fixture: ComponentFixture<WalletStatisticComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WalletStatisticComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WalletStatisticComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
