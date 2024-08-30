import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import IdeaHistoryResolve from './route/idea-history-routing-resolve.service';

const ideaHistoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/idea-history.component').then(m => m.IdeaHistoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/idea-history-detail.component').then(m => m.IdeaHistoryDetailComponent),
    resolve: {
      ideaHistory: IdeaHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/idea-history-update.component').then(m => m.IdeaHistoryUpdateComponent),
    resolve: {
      ideaHistory: IdeaHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/idea-history-update.component').then(m => m.IdeaHistoryUpdateComponent),
    resolve: {
      ideaHistory: IdeaHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ideaHistoryRoute;
