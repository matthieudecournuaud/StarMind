import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import IdeaChatResolve from './route/idea-chat-routing-resolve.service';

const ideaChatRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/idea-chat.component').then(m => m.IdeaChatComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/idea-chat-detail.component').then(m => m.IdeaChatDetailComponent),
    resolve: {
      ideaChat: IdeaChatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/idea-chat-update.component').then(m => m.IdeaChatUpdateComponent),
    resolve: {
      ideaChat: IdeaChatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/idea-chat-update.component').then(m => m.IdeaChatUpdateComponent),
    resolve: {
      ideaChat: IdeaChatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ideaChatRoute;
