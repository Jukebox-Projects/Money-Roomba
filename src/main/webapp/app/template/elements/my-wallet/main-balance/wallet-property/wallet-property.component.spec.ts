import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WalletPropertyComponent } from './wallet-property.component';

describe('WalletPropertyComponent', () => {
  let component: WalletPropertyComponent;
  let fixture: ComponentFixture<WalletPropertyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WalletPropertyComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WalletPropertyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
