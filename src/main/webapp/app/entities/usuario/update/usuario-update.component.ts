import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { UsuarioFormService, UsuarioFormGroup } from './usuario-form.service';
import { IUsuario } from '../usuario.model';
import { UsuarioService } from '../service/usuario.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IConexao } from 'app/entities/conexao/conexao.model';
import { ConexaoService } from 'app/entities/conexao/service/conexao.service';
import { IApi } from 'app/entities/api/api.model';
import { ApiService } from 'app/entities/api/service/api.service';

@Component({
  selector: 'jhi-usuario-update',
  templateUrl: './usuario-update.component.html',
})
export class UsuarioUpdateComponent implements OnInit {
  isSaving = false;
  usuario: IUsuario | null = null;

  usersSharedCollection: IUser[] = [];
  conexaosSharedCollection: IConexao[] = [];
  apisSharedCollection: IApi[] = [];

  editForm: UsuarioFormGroup = this.usuarioFormService.createUsuarioFormGroup();

  constructor(
    protected usuarioService: UsuarioService,
    protected usuarioFormService: UsuarioFormService,
    protected userService: UserService,
    protected conexaoService: ConexaoService,
    protected apiService: ApiService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareConexao = (o1: IConexao | null, o2: IConexao | null): boolean => this.conexaoService.compareConexao(o1, o2);

  compareApi = (o1: IApi | null, o2: IApi | null): boolean => this.apiService.compareApi(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuario }) => {
      this.usuario = usuario;
      if (usuario) {
        this.updateForm(usuario);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuario = this.usuarioFormService.getUsuario(this.editForm);
    if (usuario.id !== null) {
      this.subscribeToSaveResponse(this.usuarioService.update(usuario));
    } else {
      this.subscribeToSaveResponse(this.usuarioService.create(usuario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuario>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(usuario: IUsuario): void {
    this.usuario = usuario;
    this.usuarioFormService.resetForm(this.editForm, usuario);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, usuario.user);
    this.conexaosSharedCollection = this.conexaoService.addConexaoToCollectionIfMissing<IConexao>(
      this.conexaosSharedCollection,
      usuario.conexoes
    );
    this.apisSharedCollection = this.apiService.addApiToCollectionIfMissing<IApi>(this.apisSharedCollection, usuario.apis);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.usuario?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.conexaoService
      .query()
      .pipe(map((res: HttpResponse<IConexao[]>) => res.body ?? []))
      .pipe(map((conexaos: IConexao[]) => this.conexaoService.addConexaoToCollectionIfMissing<IConexao>(conexaos, this.usuario?.conexoes)))
      .subscribe((conexaos: IConexao[]) => (this.conexaosSharedCollection = conexaos));

    this.apiService
      .query()
      .pipe(map((res: HttpResponse<IApi[]>) => res.body ?? []))
      .pipe(map((apis: IApi[]) => this.apiService.addApiToCollectionIfMissing<IApi>(apis, this.usuario?.apis)))
      .subscribe((apis: IApi[]) => (this.apisSharedCollection = apis));
  }
}
