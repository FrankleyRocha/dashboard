import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConexaoDetailComponent } from './conexao-detail.component';

describe('Conexao Management Detail Component', () => {
  let comp: ConexaoDetailComponent;
  let fixture: ComponentFixture<ConexaoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConexaoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ conexao: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ConexaoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ConexaoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load conexao on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.conexao).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
