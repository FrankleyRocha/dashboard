import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ConexaoFormService, ConexaoFormGroup } from './conexao-form.service';
import { IConexao } from '../conexao.model';
import { ConexaoService } from '../service/conexao.service';

@Component({
  selector: 'jhi-conexao-update',
  templateUrl: './conexao-update.component.html',
})
export class ConexaoUpdateComponent implements OnInit {
  isSaving = false;
  conexao: IConexao | null = null;

  editForm: ConexaoFormGroup = this.conexaoFormService.createConexaoFormGroup();

  constructor(
    protected conexaoService: ConexaoService,
    protected conexaoFormService: ConexaoFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conexao }) => {
      this.conexao = conexao;
      if (conexao) {
        this.updateForm(conexao);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const conexao = this.conexaoFormService.getConexao(this.editForm);
    if (conexao.id !== null) {
      this.subscribeToSaveResponse(this.conexaoService.update(conexao));
    } else {
      this.subscribeToSaveResponse(this.conexaoService.create(conexao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConexao>>): void {
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

  protected updateForm(conexao: IConexao): void {
    this.conexao = conexao;
    this.conexaoFormService.resetForm(this.editForm, conexao);
  }
}
