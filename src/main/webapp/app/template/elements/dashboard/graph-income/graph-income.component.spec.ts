import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GraphIncomeComponent } from './graph-income.component';

describe('GraphIncomeComponent', () => {
  let component: GraphIncomeComponent;
  let fixture: ComponentFixture<GraphIncomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GraphIncomeComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GraphIncomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
