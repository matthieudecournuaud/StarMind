import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 'ba104b2e-c715-48c5-9652-1c71eb1a2f11',
  login: 'ohC',
};

export const sampleWithPartialData: IUser = {
  id: 'f8e753ba-0065-4309-b07e-fa0704dfcf96',
  login: 'I',
};

export const sampleWithFullData: IUser = {
  id: '705bcb77-22cf-4332-8aac-7008be2e4887',
  login: 'EuwNC',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
