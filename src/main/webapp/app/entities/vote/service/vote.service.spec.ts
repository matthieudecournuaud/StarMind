import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IVote } from '../vote.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../vote.test-samples';

import { RestVote, VoteService } from './vote.service';

const requireRestSample: RestVote = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('Vote Service', () => {
  let service: VoteService;
  let httpMock: HttpTestingController;
  let expectedResult: IVote | IVote[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VoteService);
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

    it('should create a Vote', () => {
      const vote = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vote).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Vote', () => {
      const vote = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vote).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Vote', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Vote', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Vote', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVoteToCollectionIfMissing', () => {
      it('should add a Vote to an empty array', () => {
        const vote: IVote = sampleWithRequiredData;
        expectedResult = service.addVoteToCollectionIfMissing([], vote);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vote);
      });

      it('should not add a Vote to an array that contains it', () => {
        const vote: IVote = sampleWithRequiredData;
        const voteCollection: IVote[] = [
          {
            ...vote,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVoteToCollectionIfMissing(voteCollection, vote);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Vote to an array that doesn't contain it", () => {
        const vote: IVote = sampleWithRequiredData;
        const voteCollection: IVote[] = [sampleWithPartialData];
        expectedResult = service.addVoteToCollectionIfMissing(voteCollection, vote);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vote);
      });

      it('should add only unique Vote to an array', () => {
        const voteArray: IVote[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const voteCollection: IVote[] = [sampleWithRequiredData];
        expectedResult = service.addVoteToCollectionIfMissing(voteCollection, ...voteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vote: IVote = sampleWithRequiredData;
        const vote2: IVote = sampleWithPartialData;
        expectedResult = service.addVoteToCollectionIfMissing([], vote, vote2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vote);
        expect(expectedResult).toContain(vote2);
      });

      it('should accept null and undefined values', () => {
        const vote: IVote = sampleWithRequiredData;
        expectedResult = service.addVoteToCollectionIfMissing([], null, vote, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vote);
      });

      it('should return initial array if no Vote is added', () => {
        const voteCollection: IVote[] = [sampleWithRequiredData];
        expectedResult = service.addVoteToCollectionIfMissing(voteCollection, undefined, null);
        expect(expectedResult).toEqual(voteCollection);
      });
    });

    describe('compareVote', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVote(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVote(entity1, entity2);
        const compareResult2 = service.compareVote(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVote(entity1, entity2);
        const compareResult2 = service.compareVote(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVote(entity1, entity2);
        const compareResult2 = service.compareVote(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
