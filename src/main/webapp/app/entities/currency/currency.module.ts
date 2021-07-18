import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CurrencyComponent } from './list/currency.component';
import { CurrencyDetailComponent } from './detail/currency-detail.component';
import { CurrencyUpdateComponent } from './update/currency-update.component';
import { CurrencyDeleteDialogComponent } from './delete/currency-delete-dialog.component';
import { CurrencyRoutingModule } from './route/currency-routing.module';
import { CurrencyStatusDialogComponent } from './status/currency-status-dialog.component';

@NgModule({
  imports: [SharedModule, CurrencyRoutingModule],
  declarations: [
    CurrencyComponent,
    CurrencyDetailComponent,
    CurrencyUpdateComponent,
    CurrencyDeleteDialogComponent,
    CurrencyStatusDialogComponent,
  ],
  entryComponents: [CurrencyDeleteDialogComponent],
})
export class CurrencyModule {}
