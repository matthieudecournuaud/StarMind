import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import RewardResolve from './route/reward-routing-resolve.service';

const rewardRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/reward.component').then(m => m.RewardComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/reward-detail.component').then(m => m.RewardDetailComponent),
    resolve: {
      reward: RewardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/reward-update.component').then(m => m.RewardUpdateComponent),
    resolve: {
      reward: RewardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/reward-update.component').then(m => m.RewardUpdateComponent),
    resolve: {
      reward: RewardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default rewardRoute;
