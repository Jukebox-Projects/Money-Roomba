import { TestBed } from '@angular/core/testing';

import { TotalBalanceService } from './total-balance.service';

describe('TotalBalanceService', () => {
  let service: TotalBalanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TotalBalanceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
