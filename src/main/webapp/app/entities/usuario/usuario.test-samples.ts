import { IUsuario, NewUsuario } from './usuario.model';

export const sampleWithRequiredData: IUsuario = {
  id: 19585,
  hash: 'cross-platform',
};

export const sampleWithPartialData: IUsuario = {
  id: 75948,
  hash: 'Alabama',
};

export const sampleWithFullData: IUsuario = {
  id: 76991,
  hash: 'Berkshire cyan auxiliary',
};

export const sampleWithNewData: NewUsuario = {
  hash: 'Litas',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
