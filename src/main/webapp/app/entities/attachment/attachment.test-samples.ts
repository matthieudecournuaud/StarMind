import { IAttachment, NewAttachment } from './attachment.model';

export const sampleWithRequiredData: IAttachment = {
  id: 22489,
  fileName: 'désagréable guide',
  fileType: 'prêter direction',
  data: '../fake-data/blob/hipster.png',
  dataContentType: 'unknown',
};

export const sampleWithPartialData: IAttachment = {
  id: 16369,
  fileName: 'oups',
  fileType: 'jeune enfant camarade fidèle',
  data: '../fake-data/blob/hipster.png',
  dataContentType: 'unknown',
};

export const sampleWithFullData: IAttachment = {
  id: 10411,
  fileName: 'prestataire de services',
  fileType: 'de façon à ce que porte-parole même si',
  data: '../fake-data/blob/hipster.png',
  dataContentType: 'unknown',
};

export const sampleWithNewData: NewAttachment = {
  fileName: 'areu areu tenter',
  fileType: 'tellement',
  data: '../fake-data/blob/hipster.png',
  dataContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
