import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import RewardHistoryResolve from './route/reward-history-routing-resolve.service';

const rewardHistoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/reward-history.component').then(m => m.RewardHistoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/reward-history-detail.component').then(m => m.RewardHistoryDetailComponent),
    resolve: {
      rewardHistory: RewardHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/reward-history-update.component').then(m => m.RewardHistoryUpdateComponent),
    resolve: {
      rewardHistory: RewardHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/reward-history-update.component').then(m => m.RewardHistoryUpdateComponent),
    resolve: {
      rewardHistory: RewardHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default rewardHistoryRoute;
