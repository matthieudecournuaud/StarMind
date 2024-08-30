import dayjs from 'dayjs/esm';

import { IRewardHistory, NewRewardHistory } from './reward-history.model';

export const sampleWithRequiredData: IRewardHistory = {
  id: 30972,
  action: 'redire parce que',
  actionDate: dayjs('2024-08-30T02:23'),
};

export const sampleWithPartialData: IRewardHistory = {
  id: 32576,
  action: 'à peu près corps enseignant',
  actionDate: dayjs('2024-08-30T10:40'),
};

export const sampleWithFullData: IRewardHistory = {
  id: 3742,
  action: 'euh',
  actionDate: dayjs('2024-08-29T14:39'),
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewRewardHistory = {
  action: 'hi',
  actionDate: dayjs('2024-08-30T02:36'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
