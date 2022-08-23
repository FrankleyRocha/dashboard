import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../conexao.test-samples';

import { ConexaoFormService } from './conexao-form.service';

describe('Conexao Form Service', () => {
  let service: ConexaoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConexaoFormService);
  });

  describe('Service methods', () => {
    describe('createConexaoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConexaoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            hash: expect.any(Object),
            url: expect.any(Object),
            usuario: expect.any(Object),
            senha: expect.any(Object),
            banco: expect.any(Object),
            schema: expect.any(Object),
          })
        );
      });

      it('passing IConexao should create a new form with FormGroup', () => {
        const formGroup = service.createConexaoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            hash: expect.any(Object),
            url: expect.any(Object),
            usuario: expect.any(Object),
            senha: expect.any(Object),
            banco: expect.any(Object),
            schema: expect.any(Object),
          })
        );
      });
    });

    describe('getConexao', () => {
      it('should return NewConexao for default Conexao initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createConexaoFormGroup(sampleWithNewData);

        const conexao = service.getConexao(formGroup) as any;

        expect(conexao).toMatchObject(sampleWithNewData);
      });

      it('should return NewConexao for empty Conexao initial value', () => {
        const formGroup = service.createConexaoFormGroup();

        const conexao = service.getConexao(formGroup) as any;

        expect(conexao).toMatchObject({});
      });

      it('should return IConexao', () => {
        const formGroup = service.createConexaoFormGroup(sampleWithRequiredData);

        const conexao = service.getConexao(formGroup) as any;

        expect(conexao).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConexao should not enable id FormControl', () => {
        const formGroup = service.createConexaoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConexao should disable id FormControl', () => {
        const formGroup = service.createConexaoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
