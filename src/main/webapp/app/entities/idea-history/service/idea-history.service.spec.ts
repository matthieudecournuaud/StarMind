import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IIdeaHistory } from '../idea-history.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../idea-history.test-samples';

import { IdeaHistoryService, RestIdeaHistory } from './idea-history.service';

const requireRestSample: RestIdeaHistory = {
  ...sampleWithRequiredData,
  actionDate: sampleWithRequiredData.actionDate?.toJSON(),
};

describe('IdeaHistory Service', () => {
  let service: IdeaHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IIdeaHistory | IIdeaHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(IdeaHistoryService);
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

    it('should create a IdeaHistory', () => {
      const ideaHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ideaHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IdeaHistory', () => {
      const ideaHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ideaHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IdeaHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IdeaHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IdeaHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIdeaHistoryToCollectionIfMissing', () => {
      it('should add a IdeaHistory to an empty array', () => {
        const ideaHistory: IIdeaHistory = sampleWithRequiredData;
        expectedResult = service.addIdeaHistoryToCollectionIfMissing([], ideaHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ideaHistory);
      });

      it('should not add a IdeaHistory to an array that contains it', () => {
        const ideaHistory: IIdeaHistory = sampleWithRequiredData;
        const ideaHistoryCollection: IIdeaHistory[] = [
          {
            ...ideaHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIdeaHistoryToCollectionIfMissing(ideaHistoryCollection, ideaHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IdeaHistory to an array that doesn't contain it", () => {
        const ideaHistory: IIdeaHistory = sampleWithRequiredData;
        const ideaHistoryCollection: IIdeaHistory[] = [sampleWithPartialData];
        expectedResult = service.addIdeaHistoryToCollectionIfMissing(ideaHistoryCollection, ideaHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ideaHistory);
      });

      it('should add only unique IdeaHistory to an array', () => {
        const ideaHistoryArray: IIdeaHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ideaHistoryCollection: IIdeaHistory[] = [sampleWithRequiredData];
        expectedResult = service.addIdeaHistoryToCollectionIfMissing(ideaHistoryCollection, ...ideaHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ideaHistory: IIdeaHistory = sampleWithRequiredData;
        const ideaHistory2: IIdeaHistory = sampleWithPartialData;
        expectedResult = service.addIdeaHistoryToCollectionIfMissing([], ideaHistory, ideaHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ideaHistory);
        expect(expectedResult).toContain(ideaHistory2);
      });

      it('should accept null and undefined values', () => {
        const ideaHistory: IIdeaHistory = sampleWithRequiredData;
        expectedResult = service.addIdeaHistoryToCollectionIfMissing([], null, ideaHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ideaHistory);
      });

      it('should return initial array if no IdeaHistory is added', () => {
        const ideaHistoryCollection: IIdeaHistory[] = [sampleWithRequiredData];
        expectedResult = service.addIdeaHistoryToCollectionIfMissing(ideaHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(ideaHistoryCollection);
      });
    });

    describe('compareIdeaHistory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIdeaHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIdeaHistory(entity1, entity2);
        const compareResult2 = service.compareIdeaHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIdeaHistory(entity1, entity2);
        const compareResult2 = service.compareIdeaHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIdeaHistory(entity1, entity2);
        const compareResult2 = service.compareIdeaHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
