import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConexaoFormService } from './conexao-form.service';
import { ConexaoService } from '../service/conexao.service';
import { IConexao } from '../conexao.model';

import { ConexaoUpdateComponent } from './conexao-update.component';

describe('Conexao Management Update Component', () => {
  let comp: ConexaoUpdateComponent;
  let fixture: ComponentFixture<ConexaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let conexaoFormService: ConexaoFormService;
  let conexaoService: ConexaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConexaoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ConexaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConexaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    conexaoFormService = TestBed.inject(ConexaoFormService);
    conexaoService = TestBed.inject(ConexaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const conexao: IConexao = { id: 456 };

      activatedRoute.data = of({ conexao });
      comp.ngOnInit();

      expect(comp.conexao).toEqual(conexao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConexao>>();
      const conexao = { id: 123 };
      jest.spyOn(conexaoFormService, 'getConexao').mockReturnValue(conexao);
      jest.spyOn(conexaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conexao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conexao }));
      saveSubject.complete();

      // THEN
      expect(conexaoFormService.getConexao).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(conexaoService.update).toHaveBeenCalledWith(expect.objectContaining(conexao));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConexao>>();
      const conexao = { id: 123 };
      jest.spyOn(conexaoFormService, 'getConexao').mockReturnValue({ id: null });
      jest.spyOn(conexaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conexao: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conexao }));
      saveSubject.complete();

      // THEN
      expect(conexaoFormService.getConexao).toHaveBeenCalled();
      expect(conexaoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConexao>>();
      const conexao = { id: 123 };
      jest.spyOn(conexaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conexao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(conexaoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
