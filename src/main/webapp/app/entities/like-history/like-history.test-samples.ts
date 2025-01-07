import dayjs from 'dayjs/esm';

import { ILikeHistory, NewLikeHistory } from './like-history.model';

export const sampleWithRequiredData: ILikeHistory = {
  id: 1376,
  action: 'chef de cuisine',
  actionDate: dayjs('2024-12-09T09:41'),
};

export const sampleWithPartialData: ILikeHistory = {
  id: 32026,
  action: 'exprès',
  actionDate: dayjs('2024-12-09T10:49'),
  oldLikes: 4065,
  newLikes: 24722,
};

export const sampleWithFullData: ILikeHistory = {
  id: 17219,
  action: 'sous grâce à police',
  actionDate: dayjs('2024-12-08T23:06'),
  oldLikes: 4384,
  newLikes: 6278,
};

export const sampleWithNewData: NewLikeHistory = {
  action: 'durant',
  actionDate: dayjs('2024-12-08T15:20'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
