import dayjs from 'dayjs/esm';

import { IIdea, NewIdea } from './idea.model';

export const sampleWithRequiredData: IIdea = {
  id: 3504,
  title: 'drelin',
  description: '../fake-data/blob/hipster.txt',
  status: 'REJECTED',
  isConfidential: false,
};

export const sampleWithPartialData: IIdea = {
  id: 11589,
  title: 'intrépide',
  description: '../fake-data/blob/hipster.txt',
  status: 'IN_PROGRESS',
  isConfidential: false,
  validation: true,
  rewardType: 'DIAMANT',
  likes: 10243,
  createdDate: dayjs('2024-12-09T05:41'),
};

export const sampleWithFullData: IIdea = {
  id: 1989,
  title: 'insipide présidence jouer',
  description: '../fake-data/blob/hipster.txt',
  status: 'COMPLETED',
  isConfidential: true,
  validation: true,
  rewardType: 'OR',
  likes: 20511,
  createdDate: dayjs('2024-12-09T09:19'),
  modifiedDate: dayjs('2024-12-08T22:45'),
  isPublic: true,
  impact: 'volontiers hé du moment que',
};

export const sampleWithNewData: NewIdea = {
  title: 'auparavant',
  description: '../fake-data/blob/hipster.txt',
  status: 'REJECTED',
  isConfidential: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
