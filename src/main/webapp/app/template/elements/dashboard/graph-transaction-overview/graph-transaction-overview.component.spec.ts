import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GraphTransactionOverviewComponent } from './graph-transaction-overview.component';

describe('GraphTransactionOverviewComponent', () => {
  let component: GraphTransactionOverviewComponent;
  let fixture: ComponentFixture<GraphTransactionOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GraphTransactionOverviewComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GraphTransactionOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
