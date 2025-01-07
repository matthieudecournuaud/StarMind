import dayjs from 'dayjs/esm';

import { IComment, NewComment } from './comment.model';

export const sampleWithRequiredData: IComment = {
  id: 23314,
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IComment = {
  id: 13813,
  content: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2024-12-08T18:01'),
};

export const sampleWithFullData: IComment = {
  id: 6525,
  content: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2024-12-09T08:22'),
};

export const sampleWithNewData: NewComment = {
  content: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
