import { UserManagementService } from './../../../admin/user-management/service/user-management.service';
import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserDetails, UserDetails } from '../user-details.model';
import { UserDetailsService } from '../service/user-details.service';

@Injectable({ providedIn: 'root' })
export class UserDetailsRoutingResolveService implements Resolve<UserDetails> {
  constructor(protected service: UserManagementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<UserDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.findUserDetailsById(id).pipe(
        mergeMap((userDetails: UserDetails) => {
          if (userDetails.id) {
            return of(userDetails);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserDetails());
  }
}
