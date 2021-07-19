import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GraphWeeklyWalletUsageComponent } from './graph-weekly-wallet-usage.component';

describe('GraphWeeklyWalletUsageComponent', () => {
  let component: GraphWeeklyWalletUsageComponent;
  let fixture: ComponentFixture<GraphWeeklyWalletUsageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GraphWeeklyWalletUsageComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GraphWeeklyWalletUsageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
