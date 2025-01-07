import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 8573,
  name: 'guide',
};

export const sampleWithPartialData: ICategory = {
  id: 8648,
  name: 'commis de cuisine',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ICategory = {
  id: 5390,
  name: 'impromptu communauté étudiante derechef',
  description: '../fake-data/blob/hipster.txt',
  level: 'lunatique cocorico',
};

export const sampleWithNewData: NewCategory = {
  name: 'brusque badaboum rudement',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
