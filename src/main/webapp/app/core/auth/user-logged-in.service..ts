import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Injectable, isDevMode } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { AccountService } from 'app/core/auth/account.service';
import { StateStorageService } from './state-storage.service';

@Injectable({ providedIn: 'root' })
export class UserLoggedInService implements CanActivate {
  constructor(private router: Router, private accountService: AccountService) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.accountService.identity().pipe(
      map(account => {
        if (account) {
          // eslint-disable-next-line no-console
          console.log('ACCOUNT IS: ', account);
          this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
            this.router.navigate(['']);
          });
          return false;
        } else {
          this.router.navigate(['landing']);
          return true;
        }
      })
    );
  }
}
