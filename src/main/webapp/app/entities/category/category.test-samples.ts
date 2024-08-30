import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 14408,
  name: 'jusqu’à ce que',
  level: 'charitable triathlète très',
};

export const sampleWithPartialData: ICategory = {
  id: 21564,
  name: 'à condition que du moment que',
  description: '../fake-data/blob/hipster.txt',
  level: 'vis-à-vie de adepte partenaire',
};

export const sampleWithFullData: ICategory = {
  id: 21725,
  name: 'cracher',
  description: '../fake-data/blob/hipster.txt',
  level: "à l'entour de précisément modeler",
};

export const sampleWithNewData: NewCategory = {
  name: 'comme sitôt que suffisamment',
  level: 'au-dessus a',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
