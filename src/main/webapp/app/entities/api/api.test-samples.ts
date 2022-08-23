import { IApi, NewApi } from './api.model';

export const sampleWithRequiredData: IApi = {
  id: 60512,
  hash: 'Mississippi Configuration',
};

export const sampleWithPartialData: IApi = {
  id: 58771,
  hash: 'turn-key Dynamic eyeballs',
  urlBase: 'Health circuit',
  token: 'GB',
};

export const sampleWithFullData: IApi = {
  id: 67494,
  hash: 'Chips Bike',
  urlBase: 'Focused Associate',
  token: 'Computers Yuan',
};

export const sampleWithNewData: NewApi = {
  hash: 'Berkshire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
