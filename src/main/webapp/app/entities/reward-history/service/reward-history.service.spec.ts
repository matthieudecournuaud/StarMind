import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IRewardHistory } from '../reward-history.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../reward-history.test-samples';

import { RestRewardHistory, RewardHistoryService } from './reward-history.service';

const requireRestSample: RestRewardHistory = {
  ...sampleWithRequiredData,
  actionDate: sampleWithRequiredData.actionDate?.toJSON(),
};

describe('RewardHistory Service', () => {
  let service: RewardHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IRewardHistory | IRewardHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RewardHistoryService);
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

    it('should create a RewardHistory', () => {
      const rewardHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(rewardHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RewardHistory', () => {
      const rewardHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(rewardHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RewardHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RewardHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RewardHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRewardHistoryToCollectionIfMissing', () => {
      it('should add a RewardHistory to an empty array', () => {
        const rewardHistory: IRewardHistory = sampleWithRequiredData;
        expectedResult = service.addRewardHistoryToCollectionIfMissing([], rewardHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rewardHistory);
      });

      it('should not add a RewardHistory to an array that contains it', () => {
        const rewardHistory: IRewardHistory = sampleWithRequiredData;
        const rewardHistoryCollection: IRewardHistory[] = [
          {
            ...rewardHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRewardHistoryToCollectionIfMissing(rewardHistoryCollection, rewardHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RewardHistory to an array that doesn't contain it", () => {
        const rewardHistory: IRewardHistory = sampleWithRequiredData;
        const rewardHistoryCollection: IRewardHistory[] = [sampleWithPartialData];
        expectedResult = service.addRewardHistoryToCollectionIfMissing(rewardHistoryCollection, rewardHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rewardHistory);
      });

      it('should add only unique RewardHistory to an array', () => {
        const rewardHistoryArray: IRewardHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const rewardHistoryCollection: IRewardHistory[] = [sampleWithRequiredData];
        expectedResult = service.addRewardHistoryToCollectionIfMissing(rewardHistoryCollection, ...rewardHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rewardHistory: IRewardHistory = sampleWithRequiredData;
        const rewardHistory2: IRewardHistory = sampleWithPartialData;
        expectedResult = service.addRewardHistoryToCollectionIfMissing([], rewardHistory, rewardHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rewardHistory);
        expect(expectedResult).toContain(rewardHistory2);
      });

      it('should accept null and undefined values', () => {
        const rewardHistory: IRewardHistory = sampleWithRequiredData;
        expectedResult = service.addRewardHistoryToCollectionIfMissing([], null, rewardHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rewardHistory);
      });

      it('should return initial array if no RewardHistory is added', () => {
        const rewardHistoryCollection: IRewardHistory[] = [sampleWithRequiredData];
        expectedResult = service.addRewardHistoryToCollectionIfMissing(rewardHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(rewardHistoryCollection);
      });
    });

    describe('compareRewardHistory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRewardHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRewardHistory(entity1, entity2);
        const compareResult2 = service.compareRewardHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRewardHistory(entity1, entity2);
        const compareResult2 = service.compareRewardHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRewardHistory(entity1, entity2);
        const compareResult2 = service.compareRewardHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
