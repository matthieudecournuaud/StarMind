import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LikeHistoryResolve from './route/like-history-routing-resolve.service';

const likeHistoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/like-history.component').then(m => m.LikeHistoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/like-history-detail.component').then(m => m.LikeHistoryDetailComponent),
    resolve: {
      likeHistory: LikeHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/like-history-update.component').then(m => m.LikeHistoryUpdateComponent),
    resolve: {
      likeHistory: LikeHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/like-history-update.component').then(m => m.LikeHistoryUpdateComponent),
    resolve: {
      likeHistory: LikeHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default likeHistoryRoute;
