import { TranslateService } from '@ngx-translate/core';
import { Pipe, PipeTransform } from '@angular/core';
import { format } from 'prettier';

@Pipe({
  name: 'dateAgo',
  pure: true,
})
export class DateAgoPipe implements PipeTransform {
  constructor(private translateService: TranslateService) {}
  finalFormat: string[] = [''];
  cont = 0;

  transform(value: any, args?: any): any {
    if (value) {
      const seconds = Math.floor((+new Date() - +new Date(value)) / 1000);
      if (seconds < 29) {
        return 'Just now';
      }
      // less than 30 seconds ago will show as 'Just now'
      const intervals = {
        year: 31536000,
        month: 2592000,
        week: 604800,
        day: 86400,
        hour: 3600,
        minute: 60,
        second: 1,
      };
      let counter;
      for (const i in intervals) {
        counter = Math.floor(seconds / intervals[i]);
        if (counter > 0) {
          this.translateService.get(`global.time.${i}${counter === 1 ? '' : 'Plural'}`).subscribe((text: string) => {
            this.translateService.get('global.time.format', { time: text, quantity: counter }).subscribe((format: string) => {
              this.finalFormat[this.cont] = format;
              this.cont++;
            });
          });
        }
      }
    }
    return this.finalFormat[0];
  }
}
