import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactionsCategoryComponent } from './transactions-category.component';

describe('TransactionsCategoryComponent', () => {
  let component: TransactionsCategoryComponent;
  let fixture: ComponentFixture<TransactionsCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TransactionsCategoryComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TransactionsCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
