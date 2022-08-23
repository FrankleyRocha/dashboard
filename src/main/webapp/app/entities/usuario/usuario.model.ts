import { IUser } from 'app/entities/user/user.model';
import { IConexao } from 'app/entities/conexao/conexao.model';
import { IApi } from 'app/entities/api/api.model';

export interface IUsuario {
  id: number;
  hash?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  conexoes?: Pick<IConexao, 'id' | 'hash'> | null;
  apis?: Pick<IApi, 'id' | 'hash'> | null;
}

export type NewUsuario = Omit<IUsuario, 'id'> & { id: null };
