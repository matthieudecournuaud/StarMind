import { IGlobalChat, NewGlobalChat } from './global-chat.model';

export const sampleWithRequiredData: IGlobalChat = {
  id: 17880,
  name: 'tellement enfin',
};

export const sampleWithPartialData: IGlobalChat = {
  id: 13530,
  name: 'aussitôt que',
};

export const sampleWithFullData: IGlobalChat = {
  id: 16323,
  name: 'commis de cuisine',
};

export const sampleWithNewData: NewGlobalChat = {
  name: 'partenaire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
