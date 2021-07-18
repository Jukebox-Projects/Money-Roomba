import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrencyStatusDialogComponent } from './currency-status-dialog.component';

describe('CurrencyStatusDialogComponent', () => {
  let component: CurrencyStatusDialogComponent;
  let fixture: ComponentFixture<CurrencyStatusDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CurrencyStatusDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrencyStatusDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
