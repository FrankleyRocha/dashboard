import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConexao } from '../conexao.model';
import { ConexaoService } from '../service/conexao.service';

@Injectable({ providedIn: 'root' })
export class ConexaoRoutingResolveService implements Resolve<IConexao | null> {
  constructor(protected service: ConexaoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConexao | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((conexao: HttpResponse<IConexao>) => {
          if (conexao.body) {
            return of(conexao.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
