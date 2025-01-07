import dayjs from 'dayjs/esm';

import { IRewardHistory, NewRewardHistory } from './reward-history.model';

export const sampleWithRequiredData: IRewardHistory = {
  id: 10169,
  action: 'rectorat',
  actionDate: dayjs('2024-12-09T10:29'),
};

export const sampleWithPartialData: IRewardHistory = {
  id: 24655,
  action: 'hôte après',
  actionDate: dayjs('2024-12-09T11:25'),
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IRewardHistory = {
  id: 32527,
  action: 'boum',
  actionDate: dayjs('2024-12-09T01:35'),
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewRewardHistory = {
  action: 'quasi sauf',
  actionDate: dayjs('2024-12-09T04:02'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
