import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import GlobalChatResolve from './route/global-chat-routing-resolve.service';

const globalChatRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/global-chat.component').then(m => m.GlobalChatComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/global-chat-detail.component').then(m => m.GlobalChatDetailComponent),
    resolve: {
      globalChat: GlobalChatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/global-chat-update.component').then(m => m.GlobalChatUpdateComponent),
    resolve: {
      globalChat: GlobalChatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/global-chat-update.component').then(m => m.GlobalChatUpdateComponent),
    resolve: {
      globalChat: GlobalChatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default globalChatRoute;
