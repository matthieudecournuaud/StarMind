import dayjs from 'dayjs/esm';

import { IChatMessage, NewChatMessage } from './chat-message.model';

export const sampleWithRequiredData: IChatMessage = {
  id: 4492,
  message: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2024-12-08T17:13'),
};

export const sampleWithPartialData: IChatMessage = {
  id: 11787,
  message: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2024-12-08T15:02'),
  likeCount: 16113,
};

export const sampleWithFullData: IChatMessage = {
  id: 20957,
  message: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2024-12-09T12:04'),
  likeCount: 23612,
};

export const sampleWithNewData: NewChatMessage = {
  message: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2024-12-09T08:01'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
