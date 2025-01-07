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
    path: 'comment',
    data: { pageTitle: 'starMindApp.comment.home.title' },
    loadChildren: () => import('./comment/comment.routes'),
  },
  {
    path: 'category',
    data: { pageTitle: 'starMindApp.category.home.title' },
    loadChildren: () => import('./category/category.routes'),
  },
  {
    path: 'reward',
    data: { pageTitle: 'starMindApp.reward.home.title' },
    loadChildren: () => import('./reward/reward.routes'),
  },
  {
    path: 'vote',
    data: { pageTitle: 'starMindApp.vote.home.title' },
    loadChildren: () => import('./vote/vote.routes'),
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
    path: 'global-chat',
    data: { pageTitle: 'starMindApp.globalChat.home.title' },
    loadChildren: () => import('./global-chat/global-chat.routes'),
  },
  {
    path: 'idea-chat',
    data: { pageTitle: 'starMindApp.ideaChat.home.title' },
    loadChildren: () => import('./idea-chat/idea-chat.routes'),
  },
  {
    path: 'chat-message',
    data: { pageTitle: 'starMindApp.chatMessage.home.title' },
    loadChildren: () => import('./chat-message/chat-message.routes'),
  },
  {
    path: 'prospect-board',
    data: { pageTitle: 'starMindApp.prospectBoard.home.title' },
    loadChildren: () => import('./prospect-board/prospect-board.routes'),
  },
  {
    path: 'prospect-entry',
    data: { pageTitle: 'starMindApp.prospectEntry.home.title' },
    loadChildren: () => import('./prospect-entry/prospect-entry.routes'),
  },
  {
    path: 'notification',
    data: { pageTitle: 'starMindApp.notification.home.title' },
    loadChildren: () => import('./notification/notification.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
