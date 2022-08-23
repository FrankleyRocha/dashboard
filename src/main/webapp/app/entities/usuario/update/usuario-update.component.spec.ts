import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UsuarioFormService } from './usuario-form.service';
import { UsuarioService } from '../service/usuario.service';
import { IUsuario } from '../usuario.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IConexao } from 'app/entities/conexao/conexao.model';
import { ConexaoService } from 'app/entities/conexao/service/conexao.service';
import { IApi } from 'app/entities/api/api.model';
import { ApiService } from 'app/entities/api/service/api.service';

import { UsuarioUpdateComponent } from './usuario-update.component';

describe('Usuario Management Update Component', () => {
  let comp: UsuarioUpdateComponent;
  let fixture: ComponentFixture<UsuarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let usuarioFormService: UsuarioFormService;
  let usuarioService: UsuarioService;
  let userService: UserService;
  let conexaoService: ConexaoService;
  let apiService: ApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UsuarioUpdateComponent],
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
      .overrideTemplate(UsuarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UsuarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    usuarioFormService = TestBed.inject(UsuarioFormService);
    usuarioService = TestBed.inject(UsuarioService);
    userService = TestBed.inject(UserService);
    conexaoService = TestBed.inject(ConexaoService);
    apiService = TestBed.inject(ApiService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const usuario: IUsuario = { id: 456 };
      const user: IUser = { id: 13812 };
      usuario.user = user;

      const userCollection: IUser[] = [{ id: 98506 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Conexao query and add missing value', () => {
      const usuario: IUsuario = { id: 456 };
      const conexoes: IConexao = { id: 92868 };
      usuario.conexoes = conexoes;

      const conexaoCollection: IConexao[] = [{ id: 15128 }];
      jest.spyOn(conexaoService, 'query').mockReturnValue(of(new HttpResponse({ body: conexaoCollection })));
      const additionalConexaos = [conexoes];
      const expectedCollection: IConexao[] = [...additionalConexaos, ...conexaoCollection];
      jest.spyOn(conexaoService, 'addConexaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(conexaoService.query).toHaveBeenCalled();
      expect(conexaoService.addConexaoToCollectionIfMissing).toHaveBeenCalledWith(
        conexaoCollection,
        ...additionalConexaos.map(expect.objectContaining)
      );
      expect(comp.conexaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Api query and add missing value', () => {
      const usuario: IUsuario = { id: 456 };
      const apis: IApi = { id: 4793 };
      usuario.apis = apis;

      const apiCollection: IApi[] = [{ id: 97222 }];
      jest.spyOn(apiService, 'query').mockReturnValue(of(new HttpResponse({ body: apiCollection })));
      const additionalApis = [apis];
      const expectedCollection: IApi[] = [...additionalApis, ...apiCollection];
      jest.spyOn(apiService, 'addApiToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(apiService.query).toHaveBeenCalled();
      expect(apiService.addApiToCollectionIfMissing).toHaveBeenCalledWith(apiCollection, ...additionalApis.map(expect.objectContaining));
      expect(comp.apisSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const usuario: IUsuario = { id: 456 };
      const user: IUser = { id: 95854 };
      usuario.user = user;
      const conexoes: IConexao = { id: 43103 };
      usuario.conexoes = conexoes;
      const apis: IApi = { id: 97656 };
      usuario.apis = apis;

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.conexaosSharedCollection).toContain(conexoes);
      expect(comp.apisSharedCollection).toContain(apis);
      expect(comp.usuario).toEqual(usuario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsuario>>();
      const usuario = { id: 123 };
      jest.spyOn(usuarioFormService, 'getUsuario').mockReturnValue(usuario);
      jest.spyOn(usuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuario }));
      saveSubject.complete();

      // THEN
      expect(usuarioFormService.getUsuario).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(usuarioService.update).toHaveBeenCalledWith(expect.objectContaining(usuario));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsuario>>();
      const usuario = { id: 123 };
      jest.spyOn(usuarioFormService, 'getUsuario').mockReturnValue({ id: null });
      jest.spyOn(usuarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuario }));
      saveSubject.complete();

      // THEN
      expect(usuarioFormService.getUsuario).toHaveBeenCalled();
      expect(usuarioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsuario>>();
      const usuario = { id: 123 };
      jest.spyOn(usuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(usuarioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareConexao', () => {
      it('Should forward to conexaoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(conexaoService, 'compareConexao');
        comp.compareConexao(entity, entity2);
        expect(conexaoService.compareConexao).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareApi', () => {
      it('Should forward to apiService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(apiService, 'compareApi');
        comp.compareApi(entity, entity2);
        expect(apiService.compareApi).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
