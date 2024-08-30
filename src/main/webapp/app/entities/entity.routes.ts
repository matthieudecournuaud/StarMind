import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'starMindApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'idea',
    data: { pageTitle: 'starMindApp.idea.home.title' },
    loadChildren: () => import('./idea/idea.routes'),
  },
  {
    path: 'category',
    data: { pageTitle: 'starMindApp.category.home.title' },
    loadChildren: () => import('./category/category.routes'),
  },
  {
    path: 'comment',
    data: { pageTitle: 'starMindApp.comment.home.title' },
    loadChildren: () => import('./comment/comment.routes'),
  },
  {
    path: 'reward',
    data: { pageTitle: 'starMindApp.reward.home.title' },
    loadChildren: () => import('./reward/reward.routes'),
  },
  {
    path: 'idea-history',
    data: { pageTitle: 'starMindApp.ideaHistory.home.title' },
    loadChildren: () => import('./idea-history/idea-history.routes'),
  },
  {
    path: 'reward-history',
    data: { pageTitle: 'starMindApp.rewardHistory.home.title' },
    loadChildren: () => import('./reward-history/reward-history.routes'),
  },
  {
    path: 'like-history',
    data: { pageTitle: 'starMindApp.likeHistory.home.title' },
    loadChildren: () => import('./like-history/like-history.routes'),
  },
  {
    path: 'notification',
    data: { pageTitle: 'starMindApp.notification.home.title' },
    loadChildren: () => import('./notification/notification.routes'),
  },
  {
    path: 'attachment',
    data: { pageTitle: 'starMindApp.attachment.home.title' },
    loadChildren: () => import('./attachment/attachment.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
