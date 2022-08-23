export interface IConexao {
  id: number;
  hash?: string | null;
  url?: string | null;
  usuario?: string | null;
  senha?: string | null;
  banco?: string | null;
  schema?: string | null;
}

export type NewConexao = Omit<IConexao, 'id'> & { id: null };
