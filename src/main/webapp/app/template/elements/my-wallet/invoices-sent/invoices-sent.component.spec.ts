import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvoicesSentComponent } from './invoices-sent.component';

describe('InvoicesSentComponent', () => {
  let component: InvoicesSentComponent;
  let fixture: ComponentFixture<InvoicesSentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InvoicesSentComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InvoicesSentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
