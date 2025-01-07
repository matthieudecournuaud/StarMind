import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IProspectBoard } from '../prospect-board.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../prospect-board.test-samples';

import { ProspectBoardService, RestProspectBoard } from './prospect-board.service';

const requireRestSample: RestProspectBoard = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  modifiedDate: sampleWithRequiredData.modifiedDate?.toJSON(),
};

describe('ProspectBoard Service', () => {
  let service: ProspectBoardService;
  let httpMock: HttpTestingController;
  let expectedResult: IProspectBoard | IProspectBoard[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProspectBoardService);
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

    it('should create a ProspectBoard', () => {
      const prospectBoard = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(prospectBoard).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProspectBoard', () => {
      const prospectBoard = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(prospectBoard).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProspectBoard', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProspectBoard', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProspectBoard', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProspectBoardToCollectionIfMissing', () => {
      it('should add a ProspectBoard to an empty array', () => {
        const prospectBoard: IProspectBoard = sampleWithRequiredData;
        expectedResult = service.addProspectBoardToCollectionIfMissing([], prospectBoard);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prospectBoard);
      });

      it('should not add a ProspectBoard to an array that contains it', () => {
        const prospectBoard: IProspectBoard = sampleWithRequiredData;
        const prospectBoardCollection: IProspectBoard[] = [
          {
            ...prospectBoard,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProspectBoardToCollectionIfMissing(prospectBoardCollection, prospectBoard);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProspectBoard to an array that doesn't contain it", () => {
        const prospectBoard: IProspectBoard = sampleWithRequiredData;
        const prospectBoardCollection: IProspectBoard[] = [sampleWithPartialData];
        expectedResult = service.addProspectBoardToCollectionIfMissing(prospectBoardCollection, prospectBoard);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prospectBoard);
      });

      it('should add only unique ProspectBoard to an array', () => {
        const prospectBoardArray: IProspectBoard[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const prospectBoardCollection: IProspectBoard[] = [sampleWithRequiredData];
        expectedResult = service.addProspectBoardToCollectionIfMissing(prospectBoardCollection, ...prospectBoardArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prospectBoard: IProspectBoard = sampleWithRequiredData;
        const prospectBoard2: IProspectBoard = sampleWithPartialData;
        expectedResult = service.addProspectBoardToCollectionIfMissing([], prospectBoard, prospectBoard2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prospectBoard);
        expect(expectedResult).toContain(prospectBoard2);
      });

      it('should accept null and undefined values', () => {
        const prospectBoard: IProspectBoard = sampleWithRequiredData;
        expectedResult = service.addProspectBoardToCollectionIfMissing([], null, prospectBoard, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prospectBoard);
      });

      it('should return initial array if no ProspectBoard is added', () => {
        const prospectBoardCollection: IProspectBoard[] = [sampleWithRequiredData];
        expectedResult = service.addProspectBoardToCollectionIfMissing(prospectBoardCollection, undefined, null);
        expect(expectedResult).toEqual(prospectBoardCollection);
      });
    });

    describe('compareProspectBoard', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProspectBoard(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProspectBoard(entity1, entity2);
        const compareResult2 = service.compareProspectBoard(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProspectBoard(entity1, entity2);
        const compareResult2 = service.compareProspectBoard(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProspectBoard(entity1, entity2);
        const compareResult2 = service.compareProspectBoard(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
