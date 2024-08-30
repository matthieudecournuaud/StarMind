import dayjs from 'dayjs/esm';

import { IComment, NewComment } from './comment.model';

export const sampleWithRequiredData: IComment = {
  id: 19354,
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IComment = {
  id: 20727,
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IComment = {
  id: 5851,
  content: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2024-08-29T21:43'),
};

export const sampleWithNewData: NewComment = {
  content: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
