import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserScheduledTransactionsComponent } from './user-scheduled-transactions.component';

describe('UserScheduledTransactionsComponent', () => {
  let component: UserScheduledTransactionsComponent;
  let fixture: ComponentFixture<UserScheduledTransactionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserScheduledTransactionsComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserScheduledTransactionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
