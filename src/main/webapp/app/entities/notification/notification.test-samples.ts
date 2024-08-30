import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 13260,
  message: 'bien que derrière crac',
  type: 'que',
  sentDate: dayjs('2024-08-30T02:42'),
};

export const sampleWithPartialData: INotification = {
  id: 23894,
  message: 'coac coac',
  type: 'coupable âcre',
  sentDate: dayjs('2024-08-30T10:34'),
  read: true,
};

export const sampleWithFullData: INotification = {
  id: 2160,
  message: 'emmener',
  type: 'désirer areu areu miaou',
  sentDate: dayjs('2024-08-29T14:24'),
  read: true,
};

export const sampleWithNewData: NewNotification = {
  message: 'du fait que',
  type: 'avex responsable gigantesque',
  sentDate: dayjs('2024-08-30T04:02'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
