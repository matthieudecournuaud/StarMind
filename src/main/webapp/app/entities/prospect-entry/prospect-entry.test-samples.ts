import dayjs from 'dayjs/esm';

import { IProspectEntry, NewProspectEntry } from './prospect-entry.model';

export const sampleWithRequiredData: IProspectEntry = {
  id: 32761,
  status: 'NEW',
  createdDate: dayjs('2024-12-08T19:25'),
  modifiedDate: dayjs('2024-12-09T07:54'),
};

export const sampleWithPartialData: IProspectEntry = {
  id: 17515,
  status: 'REJECTED',
  createdDate: dayjs('2024-12-09T02:26'),
  modifiedDate: dayjs('2024-12-09T04:44'),
};

export const sampleWithFullData: IProspectEntry = {
  id: 19585,
  status: 'NEW',
  createdDate: dayjs('2024-12-09T02:09'),
  modifiedDate: dayjs('2024-12-09T11:55'),
};

export const sampleWithNewData: NewProspectEntry = {
  status: 'CONTACTED',
  createdDate: dayjs('2024-12-08T17:56'),
  modifiedDate: dayjs('2024-12-08T19:31'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
