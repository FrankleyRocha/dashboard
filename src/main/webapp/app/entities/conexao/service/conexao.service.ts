import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConexao, NewConexao } from '../conexao.model';

export type PartialUpdateConexao = Partial<IConexao> & Pick<IConexao, 'id'>;

export type EntityResponseType = HttpResponse<IConexao>;
export type EntityArrayResponseType = HttpResponse<IConexao[]>;

@Injectable({ providedIn: 'root' })
export class ConexaoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/conexaos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(conexao: NewConexao): Observable<EntityResponseType> {
    return this.http.post<IConexao>(this.resourceUrl, conexao, { observe: 'response' });
  }

  update(conexao: IConexao): Observable<EntityResponseType> {
    return this.http.put<IConexao>(`${this.resourceUrl}/${this.getConexaoIdentifier(conexao)}`, conexao, { observe: 'response' });
  }

  partialUpdate(conexao: PartialUpdateConexao): Observable<EntityResponseType> {
    return this.http.patch<IConexao>(`${this.resourceUrl}/${this.getConexaoIdentifier(conexao)}`, conexao, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConexao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConexao[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConexaoIdentifier(conexao: Pick<IConexao, 'id'>): number {
    return conexao.id;
  }

  compareConexao(o1: Pick<IConexao, 'id'> | null, o2: Pick<IConexao, 'id'> | null): boolean {
    return o1 && o2 ? this.getConexaoIdentifier(o1) === this.getConexaoIdentifier(o2) : o1 === o2;
  }

  addConexaoToCollectionIfMissing<Type extends Pick<IConexao, 'id'>>(
    conexaoCollection: Type[],
    ...conexaosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const conexaos: Type[] = conexaosToCheck.filter(isPresent);
    if (conexaos.length > 0) {
      const conexaoCollectionIdentifiers = conexaoCollection.map(conexaoItem => this.getConexaoIdentifier(conexaoItem)!);
      const conexaosToAdd = conexaos.filter(conexaoItem => {
        const conexaoIdentifier = this.getConexaoIdentifier(conexaoItem);
        if (conexaoCollectionIdentifiers.includes(conexaoIdentifier)) {
          return false;
        }
        conexaoCollectionIdentifiers.push(conexaoIdentifier);
        return true;
      });
      return [...conexaosToAdd, ...conexaoCollection];
    }
    return conexaoCollection;
  }
}
