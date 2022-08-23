import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'usuario',
        data: { pageTitle: 'dashboardApp.usuario.home.title' },
        loadChildren: () => import('./usuario/usuario.module').then(m => m.UsuarioModule),
      },
      {
        path: 'conexao',
        data: { pageTitle: 'dashboardApp.conexao.home.title' },
        loadChildren: () => import('./conexao/conexao.module').then(m => m.ConexaoModule),
      },
      {
        path: 'api',
        data: { pageTitle: 'dashboardApp.api.home.title' },
        loadChildren: () => import('./api/api.module').then(m => m.ApiModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
