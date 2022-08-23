import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConexaoComponent } from './list/conexao.component';
import { ConexaoDetailComponent } from './detail/conexao-detail.component';
import { ConexaoUpdateComponent } from './update/conexao-update.component';
import { ConexaoDeleteDialogComponent } from './delete/conexao-delete-dialog.component';
import { ConexaoRoutingModule } from './route/conexao-routing.module';

@NgModule({
  imports: [SharedModule, ConexaoRoutingModule],
  declarations: [ConexaoComponent, ConexaoDetailComponent, ConexaoUpdateComponent, ConexaoDeleteDialogComponent],
})
export class ConexaoModule {}
