import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LandingPageComponent } from './../landing/landing-page/landing-page.component';
import { UserLoggedInService } from './../core/auth/user-logged-in.service.';
import { Route } from '@angular/router';

import { HomeComponent } from './home.component';

export const HOME_ROUTE: Route = {
  path: 'landing',
  component: LandingPageComponent,
};

export const DASHBOARD_ROUTE: Route = {
  path: '',
  component: HomeComponent,
  data: {
    pageTitle: 'home.title',
  },
  canActivate: [UserRouteAccessService],
};
