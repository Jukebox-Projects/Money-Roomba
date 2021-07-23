import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CategoryComponent } from './list/category.component';
import { CategoryDetailComponent } from './detail/category-detail.component';
import { CategoryUpdateComponent } from './update/category-update.component';
import { CategoryDeleteDialogComponent } from './delete/category-delete-dialog.component';
import { CategoryRoutingModule } from './route/category-routing.module';
import { CategoryStatusDialogComponent } from './status/category-status-dialog.component';
import { IconService } from '../../shared/icon-picker/service/icon.service';

@NgModule({
  imports: [SharedModule, CategoryRoutingModule],
  declarations: [
    CategoryComponent,
    CategoryDetailComponent,
    CategoryUpdateComponent,
    CategoryDeleteDialogComponent,
    CategoryStatusDialogComponent,
  ],
  entryComponents: [CategoryDeleteDialogComponent, CategoryStatusDialogComponent],
  providers: [IconService],
})
export class CategoryModule {}
