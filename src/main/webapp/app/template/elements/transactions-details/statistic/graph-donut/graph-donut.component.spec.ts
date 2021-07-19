import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GraphDonutComponent } from './graph-donut.component';

describe('GraphDonutComponent', () => {
  let component: GraphDonutComponent;
  let fixture: ComponentFixture<GraphDonutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GraphDonutComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GraphDonutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
