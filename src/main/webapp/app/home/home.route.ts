import { Route } from '@angular/router';

import { HomeComponent } from './home.component';
import { Authority } from '../../app/config/authority.constants';
import { UserRouteAccessService } from '../../app/core/auth/user-route-access.service';

export const HOME_ROUTE: Route = {
  path: '',
  component: HomeComponent,
  data: {
    pageTitle: 'home.title',
    authorities: [Authority.ADMIN],
  },
  canActivate: [UserRouteAccessService],
};
