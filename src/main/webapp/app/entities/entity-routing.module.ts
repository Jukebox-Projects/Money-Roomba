import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'user-details',
        data: { pageTitle: 'moneyRoombaApp.userDetails.home.title' },
        loadChildren: () => import('./user-details/user-details.module').then(m => m.UserDetailsModule),
      },
      {
        path: 'wallet',
        data: { pageTitle: 'moneyRoombaApp.wallet.home.title' },
        loadChildren: () => import('./wallet/wallet.module').then(m => m.WalletModule),
      },
      {
        path: 'transaction',
        data: { pageTitle: 'moneyRoombaApp.transaction.home.title' },
        loadChildren: () => import('./transaction/transaction.module').then(m => m.TransactionModule),
      },
      {
        path: 'scheduled-transaction',
        data: { pageTitle: 'moneyRoombaApp.scheduledTransaction.home.title' },
        loadChildren: () => import('./scheduled-transaction/scheduled-transaction.module').then(m => m.ScheduledTransactionModule),
      },
      {
        path: 'schedule-pattern',
        data: { pageTitle: 'moneyRoombaApp.schedulePattern.home.title' },
        loadChildren: () => import('./schedule-pattern/schedule-pattern.module').then(m => m.SchedulePatternModule),
      },
      {
        path: 'attachment',
        data: { pageTitle: 'moneyRoombaApp.attachment.home.title' },
        loadChildren: () => import('./attachment/attachment.module').then(m => m.AttachmentModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'moneyRoombaApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'invoice',
        data: { pageTitle: 'moneyRoombaApp.invoice.home.title' },
        loadChildren: () => import('./invoice/invoice.module').then(m => m.InvoiceModule),
      },
      {
        path: 'license',
        data: { pageTitle: 'moneyRoombaApp.license.home.title' },
        loadChildren: () => import('./license/license.module').then(m => m.LicenseModule),
      },
      {
        path: 'currency',
        data: { pageTitle: 'moneyRoombaApp.currency.home.title' },
        loadChildren: () => import('./currency/currency.module').then(m => m.CurrencyModule),
      },
      {
        path: 'notification',
        data: { pageTitle: 'moneyRoombaApp.notification.home.title' },
        loadChildren: () => import('./notification/notification.module').then(m => m.NotificationModule),
      },
      {
        path: 'event',
        data: { pageTitle: 'moneyRoombaApp.event.home.title' },
        loadChildren: () => import('./event/event.module').then(m => m.EventModule),
      },
      {
        path: 'system-setting',
        data: { pageTitle: 'moneyRoombaApp.systemSetting.home.title' },
        loadChildren: () => import('./system-setting/system-setting.module').then(m => m.SystemSettingModule),
      },
      {
        path: 'currency-converter',
        data: { pageTitle: 'moneyRoombaApp.currencyConverter.home.title' },
        loadChildren: () => import('./currency-converter/currency-converter.module').then(m => m.CurrencyConverterModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
