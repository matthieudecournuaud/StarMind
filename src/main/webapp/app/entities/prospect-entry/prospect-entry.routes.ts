import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProspectEntryResolve from './route/prospect-entry-routing-resolve.service';

const prospectEntryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/prospect-entry.component').then(m => m.ProspectEntryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/prospect-entry-detail.component').then(m => m.ProspectEntryDetailComponent),
    resolve: {
      prospectEntry: ProspectEntryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/prospect-entry-update.component').then(m => m.ProspectEntryUpdateComponent),
    resolve: {
      prospectEntry: ProspectEntryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/prospect-entry-update.component').then(m => m.ProspectEntryUpdateComponent),
    resolve: {
      prospectEntry: ProspectEntryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default prospectEntryRoute;
