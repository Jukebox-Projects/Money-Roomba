import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CurrencyConverterComponent } from '../currency-converter.component';

const currencyConverterRoute: Routes = [
  {
    path: '',
    component: CurrencyConverterComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(currencyConverterRoute)],
  exports: [RouterModule],
})
export class CurrencyConverterRoutingModule {}
