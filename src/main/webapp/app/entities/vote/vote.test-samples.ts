import dayjs from 'dayjs/esm';

import { IVote, NewVote } from './vote.model';

export const sampleWithRequiredData: IVote = {
  id: 25066,
  voteOption: 'CONTRE',
  createdDate: dayjs('2024-12-09T04:36'),
};

export const sampleWithPartialData: IVote = {
  id: 23378,
  voteOption: 'NEUTRE',
  comment: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2024-12-09T00:58'),
};

export const sampleWithFullData: IVote = {
  id: 5046,
  voteOption: 'NEUTRE',
  comment: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2024-12-08T20:26'),
};

export const sampleWithNewData: NewVote = {
  voteOption: 'CONTRE',
  createdDate: dayjs('2024-12-09T01:27'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
