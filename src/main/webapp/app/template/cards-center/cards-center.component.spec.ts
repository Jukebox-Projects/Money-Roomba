import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardsCenterComponent } from './cards-center.component';

describe('CardsCenterComponent', () => {
  let component: CardsCenterComponent;
  let fixture: ComponentFixture<CardsCenterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CardsCenterComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CardsCenterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
