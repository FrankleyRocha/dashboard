import { IConexao, NewConexao } from './conexao.model';

export const sampleWithRequiredData: IConexao = {
  id: 53123,
  hash: 'Yemen',
};

export const sampleWithPartialData: IConexao = {
  id: 77066,
  hash: 'Designer Toys',
  url: 'https://elvis.info',
  usuario: 'Data Ball',
  banco: 'COM',
  schema: 'interactive Wooden override',
};

export const sampleWithFullData: IConexao = {
  id: 88771,
  hash: 'Denar quantifying COM',
  url: 'http://birdie.biz',
  usuario: 'indexing',
  senha: 'efficient',
  banco: 'Cambridgeshire Web maroon',
  schema: 'models recontextualize',
};

export const sampleWithNewData: NewConexao = {
  hash: 'Card',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
