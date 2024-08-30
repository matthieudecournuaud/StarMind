import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import IdeaResolve from './route/idea-routing-resolve.service';

const ideaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/idea.component').then(m => m.IdeaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/idea-detail.component').then(m => m.IdeaDetailComponent),
    resolve: {
      idea: IdeaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/idea-update.component').then(m => m.IdeaUpdateComponent),
    resolve: {
      idea: IdeaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/idea-update.component').then(m => m.IdeaUpdateComponent),
    resolve: {
      idea: IdeaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ideaRoute;
