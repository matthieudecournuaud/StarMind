import dayjs from 'dayjs/esm';

import { IProspectBoard, NewProspectBoard } from './prospect-board.model';

export const sampleWithRequiredData: IProspectBoard = {
  id: 24995,
  name: 'quand afin de de façon à ce que',
  createdDate: dayjs('2024-12-08T23:40'),
  modifiedDate: dayjs('2024-12-08T17:31'),
};

export const sampleWithPartialData: IProspectBoard = {
  id: 12702,
  name: 'assez bof laver',
  description: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2024-12-09T13:02'),
  modifiedDate: dayjs('2024-12-08T17:16'),
};

export const sampleWithFullData: IProspectBoard = {
  id: 14219,
  name: 'ouf',
  description: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2024-12-08T22:15'),
  modifiedDate: dayjs('2024-12-08T18:58'),
};

export const sampleWithNewData: NewProspectBoard = {
  name: 'derrière du moment que',
  createdDate: dayjs('2024-12-08T15:47'),
  modifiedDate: dayjs('2024-12-08T21:31'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
