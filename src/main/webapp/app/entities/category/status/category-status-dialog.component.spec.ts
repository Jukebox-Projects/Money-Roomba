import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoryStatusDialogComponent } from './category-status-dialog.component';

describe('CategoryStatusDialogComponent', () => {
  let component: CategoryStatusDialogComponent;
  let fixture: ComponentFixture<CategoryStatusDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CategoryStatusDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CategoryStatusDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
