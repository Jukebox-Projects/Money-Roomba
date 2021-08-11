import { TestBed } from '@angular/core/testing';

import { TransactionsCategoryService } from './transactions-category.service';

describe('TransactionsCategoryService', () => {
  let service: TransactionsCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransactionsCategoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
