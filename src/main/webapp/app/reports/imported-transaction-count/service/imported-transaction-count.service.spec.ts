import { TestBed } from '@angular/core/testing';

import { ImportedTransactionCountService } from './imported-transaction-count.service';

describe('TransactionCountService', () => {
  let service: ImportedTransactionCountService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImportedTransactionCountService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
