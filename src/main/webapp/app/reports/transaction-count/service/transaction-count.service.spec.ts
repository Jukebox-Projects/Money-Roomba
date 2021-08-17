import { TestBed } from '@angular/core/testing';

import { TransactionCountService } from './transaction-count.service';

describe('TransactionCountService', () => {
  let service: TransactionCountService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransactionCountService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
