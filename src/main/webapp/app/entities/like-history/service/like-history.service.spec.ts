import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ILikeHistory } from '../like-history.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../like-history.test-samples';

import { LikeHistoryService, RestLikeHistory } from './like-history.service';

const requireRestSample: RestLikeHistory = {
  ...sampleWithRequiredData,
  actionDate: sampleWithRequiredData.actionDate?.toJSON(),
};

describe('LikeHistory Service', () => {
  let service: LikeHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: ILikeHistory | ILikeHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LikeHistoryService);
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

    it('should create a LikeHistory', () => {
      const likeHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(likeHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LikeHistory', () => {
      const likeHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(likeHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LikeHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LikeHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LikeHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLikeHistoryToCollectionIfMissing', () => {
      it('should add a LikeHistory to an empty array', () => {
        const likeHistory: ILikeHistory = sampleWithRequiredData;
        expectedResult = service.addLikeHistoryToCollectionIfMissing([], likeHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(likeHistory);
      });

      it('should not add a LikeHistory to an array that contains it', () => {
        const likeHistory: ILikeHistory = sampleWithRequiredData;
        const likeHistoryCollection: ILikeHistory[] = [
          {
            ...likeHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLikeHistoryToCollectionIfMissing(likeHistoryCollection, likeHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LikeHistory to an array that doesn't contain it", () => {
        const likeHistory: ILikeHistory = sampleWithRequiredData;
        const likeHistoryCollection: ILikeHistory[] = [sampleWithPartialData];
        expectedResult = service.addLikeHistoryToCollectionIfMissing(likeHistoryCollection, likeHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(likeHistory);
      });

      it('should add only unique LikeHistory to an array', () => {
        const likeHistoryArray: ILikeHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const likeHistoryCollection: ILikeHistory[] = [sampleWithRequiredData];
        expectedResult = service.addLikeHistoryToCollectionIfMissing(likeHistoryCollection, ...likeHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const likeHistory: ILikeHistory = sampleWithRequiredData;
        const likeHistory2: ILikeHistory = sampleWithPartialData;
        expectedResult = service.addLikeHistoryToCollectionIfMissing([], likeHistory, likeHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(likeHistory);
        expect(expectedResult).toContain(likeHistory2);
      });

      it('should accept null and undefined values', () => {
        const likeHistory: ILikeHistory = sampleWithRequiredData;
        expectedResult = service.addLikeHistoryToCollectionIfMissing([], null, likeHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(likeHistory);
      });

      it('should return initial array if no LikeHistory is added', () => {
        const likeHistoryCollection: ILikeHistory[] = [sampleWithRequiredData];
        expectedResult = service.addLikeHistoryToCollectionIfMissing(likeHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(likeHistoryCollection);
      });
    });

    describe('compareLikeHistory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLikeHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLikeHistory(entity1, entity2);
        const compareResult2 = service.compareLikeHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLikeHistory(entity1, entity2);
        const compareResult2 = service.compareLikeHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLikeHistory(entity1, entity2);
        const compareResult2 = service.compareLikeHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
