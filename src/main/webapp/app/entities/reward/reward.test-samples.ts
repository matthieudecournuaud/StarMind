import { IReward, NewReward } from './reward.model';

export const sampleWithRequiredData: IReward = {
  id: 80,
  name: 'de façon à ce que loin',
};

export const sampleWithPartialData: IReward = {
  id: 16920,
  name: 'solitaire plaire',
  description: 'athlète glouglou',
};

export const sampleWithFullData: IReward = {
  id: 21180,
  name: 'biathlète du fait que divinement',
  description: 'selon membre de l’équipe',
};

export const sampleWithNewData: NewReward = {
  name: 'prévenir réfléchir',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
