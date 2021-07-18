import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GraphLimitComponent } from './graph-limit.component';

describe('GraphLimitComponent', () => {
  let component: GraphLimitComponent;
  let fixture: ComponentFixture<GraphLimitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GraphLimitComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GraphLimitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
