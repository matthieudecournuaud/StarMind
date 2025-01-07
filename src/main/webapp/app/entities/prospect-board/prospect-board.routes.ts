import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProspectBoardResolve from './route/prospect-board-routing-resolve.service';

const prospectBoardRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/prospect-board.component').then(m => m.ProspectBoardComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/prospect-board-detail.component').then(m => m.ProspectBoardDetailComponent),
    resolve: {
      prospectBoard: ProspectBoardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/prospect-board-update.component').then(m => m.ProspectBoardUpdateComponent),
    resolve: {
      prospectBoard: ProspectBoardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/prospect-board-update.component').then(m => m.ProspectBoardUpdateComponent),
    resolve: {
      prospectBoard: ProspectBoardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default prospectBoardRoute;
