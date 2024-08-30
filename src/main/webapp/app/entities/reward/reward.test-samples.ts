import { IReward, NewReward } from './reward.model';

export const sampleWithRequiredData: IReward = {
  id: 6132,
  name: 'tchou tchouu ouille au-dedans de',
};

export const sampleWithPartialData: IReward = {
  id: 11585,
  name: 'applaudir snif égarer',
  description: 'où lâche oh',
};

export const sampleWithFullData: IReward = {
  id: 5962,
  name: 'mélancolique',
  description: 'dring',
};

export const sampleWithNewData: NewReward = {
  name: 'quasi porte-parole',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
