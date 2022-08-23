import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IConexao, NewConexao } from '../conexao.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConexao for edit and NewConexaoFormGroupInput for create.
 */
type ConexaoFormGroupInput = IConexao | PartialWithRequiredKeyOf<NewConexao>;

type ConexaoFormDefaults = Pick<NewConexao, 'id'>;

type ConexaoFormGroupContent = {
  id: FormControl<IConexao['id'] | NewConexao['id']>;
  hash: FormControl<IConexao['hash']>;
  url: FormControl<IConexao['url']>;
  usuario: FormControl<IConexao['usuario']>;
  senha: FormControl<IConexao['senha']>;
  banco: FormControl<IConexao['banco']>;
  schema: FormControl<IConexao['schema']>;
};

export type ConexaoFormGroup = FormGroup<ConexaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConexaoFormService {
  createConexaoFormGroup(conexao: ConexaoFormGroupInput = { id: null }): ConexaoFormGroup {
    const conexaoRawValue = {
      ...this.getFormDefaults(),
      ...conexao,
    };
    return new FormGroup<ConexaoFormGroupContent>({
      id: new FormControl(
        { value: conexaoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      hash: new FormControl(conexaoRawValue.hash, {
        validators: [Validators.required],
      }),
      url: new FormControl(conexaoRawValue.url),
      usuario: new FormControl(conexaoRawValue.usuario),
      senha: new FormControl(conexaoRawValue.senha),
      banco: new FormControl(conexaoRawValue.banco),
      schema: new FormControl(conexaoRawValue.schema),
    });
  }

  getConexao(form: ConexaoFormGroup): IConexao | NewConexao {
    return form.getRawValue() as IConexao | NewConexao;
  }

  resetForm(form: ConexaoFormGroup, conexao: ConexaoFormGroupInput): void {
    const conexaoRawValue = { ...this.getFormDefaults(), ...conexao };
    form.reset(
      {
        ...conexaoRawValue,
        id: { value: conexaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ConexaoFormDefaults {
    return {
      id: null,
    };
  }
}
