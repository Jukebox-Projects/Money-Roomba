import { ImportedTransactionCountComponent } from './imported-transaction-count.component';
import { ComponentFixture, TestBed } from '@angular/core/testing';

describe('TransactionCountComponent', () => {
  let component: ImportedTransactionCountComponent;
  let fixture: ComponentFixture<ImportedTransactionCountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ImportedTransactionCountComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportedTransactionCountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
