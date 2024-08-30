import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '0cc9d7aa-6387-4be3-81b6-a4400bc13f68',
};

export const sampleWithPartialData: IAuthority = {
  name: 'b9f7550b-abd6-478f-a14c-484f08488217',
};

export const sampleWithFullData: IAuthority = {
  name: 'df380adc-c200-451b-84ea-30b39cd64658',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
