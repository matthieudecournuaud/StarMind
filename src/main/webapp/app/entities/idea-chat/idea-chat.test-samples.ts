import { IIdeaChat, NewIdeaChat } from './idea-chat.model';

export const sampleWithRequiredData: IIdeaChat = {
  id: 12711,
  name: 'avant trop peu secouriste',
};

export const sampleWithPartialData: IIdeaChat = {
  id: 7865,
  name: 'moins',
};

export const sampleWithFullData: IIdeaChat = {
  id: 30162,
  name: 'également clac',
};

export const sampleWithNewData: NewIdeaChat = {
  name: 'à côté de passablement',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
