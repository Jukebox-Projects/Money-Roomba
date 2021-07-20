import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CurrencyConverterComponent } from './currency-converter.component';
import { CurrencyConverterRoutingModule } from './route/currency-converter-routing-module';

@NgModule({
  imports: [SharedModule, CurrencyConverterRoutingModule],
  declarations: [CurrencyConverterComponent],
  entryComponents: [],
})
export class CurrencyConverterModule {}
