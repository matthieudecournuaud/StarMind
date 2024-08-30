import dayjs from 'dayjs/esm';

import { ILikeHistory, NewLikeHistory } from './like-history.model';

export const sampleWithRequiredData: ILikeHistory = {
  id: 12148,
  action: 'apprendre vlan',
  actionDate: dayjs('2024-08-30T04:00'),
};

export const sampleWithPartialData: ILikeHistory = {
  id: 10828,
  action: 'du côté de',
  actionDate: dayjs('2024-08-30T00:02'),
};

export const sampleWithFullData: ILikeHistory = {
  id: 25432,
  action: 'circulaire mince',
  actionDate: dayjs('2024-08-30T00:47'),
  oldLikes: 'vide',
  newLikes: 'avant que',
};

export const sampleWithNewData: NewLikeHistory = {
  action: 'de peur que',
  actionDate: dayjs('2024-08-30T11:43'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
