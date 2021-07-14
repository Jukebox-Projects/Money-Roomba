import { Component } from '@angular/core';
import { ICategory } from '../category.model';
import { CategoryService } from '../service/category.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  templateUrl: './category-status-dialog.component.html',
})
export class CategoryStatusDialogComponent {
  category?: ICategory;

  constructor(protected categoryService: CategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmChange(id: number): void {
    this.categoryService.statusUpdate(id).subscribe(() => {
      this.activeModal.close('status changed');
    });
  }
}
