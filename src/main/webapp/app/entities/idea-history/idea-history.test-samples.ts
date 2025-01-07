import dayjs from 'dayjs/esm';

import { IIdeaHistory, NewIdeaHistory } from './idea-history.model';

export const sampleWithRequiredData: IIdeaHistory = {
  id: 12947,
  action: 'quelque plic',
  actionDate: dayjs('2024-12-09T08:52'),
};

export const sampleWithPartialData: IIdeaHistory = {
  id: 6667,
  action: 'foule',
  actionDate: dayjs('2024-12-09T03:06'),
  description: '../fake-data/blob/hipster.txt',
  rewardType: 'TITANIUM',
};

export const sampleWithFullData: IIdeaHistory = {
  id: 17124,
  action: 'tant que pallier drelin',
  actionDate: dayjs('2024-12-09T03:37'),
  description: '../fake-data/blob/hipster.txt',
  rewardType: 'ELYSIUM',
  likes: 15326,
};

export const sampleWithNewData: NewIdeaHistory = {
  action: 'ha ha du fait que direction',
  actionDate: dayjs('2024-12-09T13:43'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
