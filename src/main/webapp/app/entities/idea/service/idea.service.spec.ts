import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IIdea } from '../idea.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../idea.test-samples';

import { IdeaService, RestIdea } from './idea.service';

const requireRestSample: RestIdea = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  modifiedDate: sampleWithRequiredData.modifiedDate?.toJSON(),
};

describe('Idea Service', () => {
  let service: IdeaService;
  let httpMock: HttpTestingController;
  let expectedResult: IIdea | IIdea[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(IdeaService);
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

    it('should create a Idea', () => {
      const idea = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(idea).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Idea', () => {
      const idea = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(idea).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Idea', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Idea', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Idea', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIdeaToCollectionIfMissing', () => {
      it('should add a Idea to an empty array', () => {
        const idea: IIdea = sampleWithRequiredData;
        expectedResult = service.addIdeaToCollectionIfMissing([], idea);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(idea);
      });

      it('should not add a Idea to an array that contains it', () => {
        const idea: IIdea = sampleWithRequiredData;
        const ideaCollection: IIdea[] = [
          {
            ...idea,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIdeaToCollectionIfMissing(ideaCollection, idea);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Idea to an array that doesn't contain it", () => {
        const idea: IIdea = sampleWithRequiredData;
        const ideaCollection: IIdea[] = [sampleWithPartialData];
        expectedResult = service.addIdeaToCollectionIfMissing(ideaCollection, idea);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(idea);
      });

      it('should add only unique Idea to an array', () => {
        const ideaArray: IIdea[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ideaCollection: IIdea[] = [sampleWithRequiredData];
        expectedResult = service.addIdeaToCollectionIfMissing(ideaCollection, ...ideaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const idea: IIdea = sampleWithRequiredData;
        const idea2: IIdea = sampleWithPartialData;
        expectedResult = service.addIdeaToCollectionIfMissing([], idea, idea2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(idea);
        expect(expectedResult).toContain(idea2);
      });

      it('should accept null and undefined values', () => {
        const idea: IIdea = sampleWithRequiredData;
        expectedResult = service.addIdeaToCollectionIfMissing([], null, idea, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(idea);
      });

      it('should return initial array if no Idea is added', () => {
        const ideaCollection: IIdea[] = [sampleWithRequiredData];
        expectedResult = service.addIdeaToCollectionIfMissing(ideaCollection, undefined, null);
        expect(expectedResult).toEqual(ideaCollection);
      });
    });

    describe('compareIdea', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIdea(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIdea(entity1, entity2);
        const compareResult2 = service.compareIdea(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIdea(entity1, entity2);
        const compareResult2 = service.compareIdea(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIdea(entity1, entity2);
        const compareResult2 = service.compareIdea(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
