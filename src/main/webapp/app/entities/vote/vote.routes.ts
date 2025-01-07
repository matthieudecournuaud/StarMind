import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VoteResolve from './route/vote-routing-resolve.service';

const voteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vote.component').then(m => m.VoteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vote-detail.component').then(m => m.VoteDetailComponent),
    resolve: {
      vote: VoteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vote-update.component').then(m => m.VoteUpdateComponent),
    resolve: {
      vote: VoteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vote-update.component').then(m => m.VoteUpdateComponent),
    resolve: {
      vote: VoteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default voteRoute;
