import { TestBed } from '@angular/core/testing';

import { WalletBalanceService } from './wallet-balance.service';

describe('WalletBalanceService', () => {
  let service: WalletBalanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WalletBalanceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
