import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 16842,
  message: 'doucement contribuer aigre',
  type: 'via',
  sentDate: dayjs('2024-12-08T22:00'),
};

export const sampleWithPartialData: INotification = {
  id: 25219,
  message: 'zzzz obliger',
  type: 'membre du personnel dans la mesure où solitaire',
  sentDate: dayjs('2024-12-09T08:36'),
};

export const sampleWithFullData: INotification = {
  id: 4703,
  message: 'très dès',
  type: 'mélancolique partout certainement',
  sentDate: dayjs('2024-12-09T11:23'),
  read: false,
};

export const sampleWithNewData: NewNotification = {
  message: 'coin-coin impromptu oups',
  type: 'sortir si',
  sentDate: dayjs('2024-12-09T01:28'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
