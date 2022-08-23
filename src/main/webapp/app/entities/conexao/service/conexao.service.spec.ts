import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConexao } from '../conexao.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../conexao.test-samples';

import { ConexaoService } from './conexao.service';

const requireRestSample: IConexao = {
  ...sampleWithRequiredData,
};

describe('Conexao Service', () => {
  let service: ConexaoService;
  let httpMock: HttpTestingController;
  let expectedResult: IConexao | IConexao[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConexaoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Conexao', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const conexao = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(conexao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Conexao', () => {
      const conexao = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(conexao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Conexao', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Conexao', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Conexao', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addConexaoToCollectionIfMissing', () => {
      it('should add a Conexao to an empty array', () => {
        const conexao: IConexao = sampleWithRequiredData;
        expectedResult = service.addConexaoToCollectionIfMissing([], conexao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(conexao);
      });

      it('should not add a Conexao to an array that contains it', () => {
        const conexao: IConexao = sampleWithRequiredData;
        const conexaoCollection: IConexao[] = [
          {
            ...conexao,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConexaoToCollectionIfMissing(conexaoCollection, conexao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Conexao to an array that doesn't contain it", () => {
        const conexao: IConexao = sampleWithRequiredData;
        const conexaoCollection: IConexao[] = [sampleWithPartialData];
        expectedResult = service.addConexaoToCollectionIfMissing(conexaoCollection, conexao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(conexao);
      });

      it('should add only unique Conexao to an array', () => {
        const conexaoArray: IConexao[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const conexaoCollection: IConexao[] = [sampleWithRequiredData];
        expectedResult = service.addConexaoToCollectionIfMissing(conexaoCollection, ...conexaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const conexao: IConexao = sampleWithRequiredData;
        const conexao2: IConexao = sampleWithPartialData;
        expectedResult = service.addConexaoToCollectionIfMissing([], conexao, conexao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(conexao);
        expect(expectedResult).toContain(conexao2);
      });

      it('should accept null and undefined values', () => {
        const conexao: IConexao = sampleWithRequiredData;
        expectedResult = service.addConexaoToCollectionIfMissing([], null, conexao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(conexao);
      });

      it('should return initial array if no Conexao is added', () => {
        const conexaoCollection: IConexao[] = [sampleWithRequiredData];
        expectedResult = service.addConexaoToCollectionIfMissing(conexaoCollection, undefined, null);
        expect(expectedResult).toEqual(conexaoCollection);
      });
    });

    describe('compareConexao', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConexao(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareConexao(entity1, entity2);
        const compareResult2 = service.compareConexao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareConexao(entity1, entity2);
        const compareResult2 = service.compareConexao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareConexao(entity1, entity2);
        const compareResult2 = service.compareConexao(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
