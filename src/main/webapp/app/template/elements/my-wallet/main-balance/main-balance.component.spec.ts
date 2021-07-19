import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainBalanceComponent } from './main-balance.component';

describe('MainBalanceComponent', () => {
  let component: MainBalanceComponent;
  let fixture: ComponentFixture<MainBalanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MainBalanceComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MainBalanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
