import dayjs from 'dayjs/esm';

import { IIdeaHistory, NewIdeaHistory } from './idea-history.model';

export const sampleWithRequiredData: IIdeaHistory = {
  id: 8543,
  action: 'avant que',
  actionDate: dayjs('2024-08-30T10:05'),
};

export const sampleWithPartialData: IIdeaHistory = {
  id: 28252,
  action: 'terminer pendant responsable',
  actionDate: dayjs('2024-08-29T22:01'),
  description: '../fake-data/blob/hipster.txt',
  likes: 'sembler beaucoup',
};

export const sampleWithFullData: IIdeaHistory = {
  id: 7302,
  action: 'a juriste',
  actionDate: dayjs('2024-08-30T04:16'),
  description: '../fake-data/blob/hipster.txt',
  rewardType: 'DIAMANT',
  likes: 'aïe cot cot',
};

export const sampleWithNewData: NewIdeaHistory = {
  action: 'adversaire',
  actionDate: dayjs('2024-08-30T12:36'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
