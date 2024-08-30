import dayjs from 'dayjs/esm';

import { IIdea, NewIdea } from './idea.model';

export const sampleWithRequiredData: IIdea = {
  id: 28796,
  title: 'résoudre',
  description: '../fake-data/blob/hipster.txt',
  status: 'IN_PROGRESS',
};

export const sampleWithPartialData: IIdea = {
  id: 3534,
  title: 'fourbe extatique changer',
  description: '../fake-data/blob/hipster.txt',
  status: 'OPEN',
  rewardType: 'ARGENT',
  likes: 'retourner comme au-delà',
  createdDate: dayjs('2024-08-30T11:28'),
  modifiedDate: dayjs('2024-08-30T04:13'),
};

export const sampleWithFullData: IIdea = {
  id: 23931,
  title: 'splendide',
  description: '../fake-data/blob/hipster.txt',
  status: 'OPEN',
  validation: true,
  rewardType: 'STARMIND',
  likes: 'en face de étant donné que',
  createdDate: dayjs('2024-08-30T09:52'),
  modifiedDate: dayjs('2024-08-29T17:09'),
};

export const sampleWithNewData: NewIdea = {
  title: 'dès groin groin',
  description: '../fake-data/blob/hipster.txt',
  status: 'COMPLETED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
