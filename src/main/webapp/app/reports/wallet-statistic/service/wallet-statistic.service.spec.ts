import { TestBed } from '@angular/core/testing';

import { WalletStatisticService } from './wallet-statistic.service';

describe('WalletStatisticService', () => {
  let service: WalletStatisticService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WalletStatisticService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
