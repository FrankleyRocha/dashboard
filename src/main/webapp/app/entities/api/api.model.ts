export interface IApi {
  id: number;
  hash?: string | null;
  urlBase?: string | null;
  token?: string | null;
}

export type NewApi = Omit<IApi, 'id'> & { id: null };
