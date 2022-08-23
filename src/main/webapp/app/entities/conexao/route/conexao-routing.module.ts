import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConexaoComponent } from '../list/conexao.component';
import { ConexaoDetailComponent } from '../detail/conexao-detail.component';
import { ConexaoUpdateComponent } from '../update/conexao-update.component';
import { ConexaoRoutingResolveService } from './conexao-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const conexaoRoute: Routes = [
  {
    path: '',
    component: ConexaoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConexaoDetailComponent,
    resolve: {
      conexao: ConexaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConexaoUpdateComponent,
    resolve: {
      conexao: ConexaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConexaoUpdateComponent,
    resolve: {
      conexao: ConexaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(conexaoRoute)],
  exports: [RouterModule],
})
export class ConexaoRoutingModule {}
