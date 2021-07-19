import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GraphOutomeComponent } from './graph-outome.component';

describe('GraphOutomeComponent', () => {
  let component: GraphOutomeComponent;
  let fixture: ComponentFixture<GraphOutomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GraphOutomeComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GraphOutomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
