import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConexao } from '../conexao.model';

@Component({
  selector: 'jhi-conexao-detail',
  templateUrl: './conexao-detail.component.html',
})
export class ConexaoDetailComponent implements OnInit {
  conexao: IConexao | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conexao }) => {
      this.conexao = conexao;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
