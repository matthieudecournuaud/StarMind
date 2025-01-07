import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IProspectEntry } from '../prospect-entry.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../prospect-entry.test-samples';

import { ProspectEntryService, RestProspectEntry } from './prospect-entry.service';

const requireRestSample: RestProspectEntry = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  modifiedDate: sampleWithRequiredData.modifiedDate?.toJSON(),
};

describe('ProspectEntry Service', () => {
  let service: ProspectEntryService;
  let httpMock: HttpTestingController;
  let expectedResult: IProspectEntry | IProspectEntry[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProspectEntryService);
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

    it('should create a ProspectEntry', () => {
      const prospectEntry = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(prospectEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProspectEntry', () => {
      const prospectEntry = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(prospectEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProspectEntry', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProspectEntry', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProspectEntry', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProspectEntryToCollectionIfMissing', () => {
      it('should add a ProspectEntry to an empty array', () => {
        const prospectEntry: IProspectEntry = sampleWithRequiredData;
        expectedResult = service.addProspectEntryToCollectionIfMissing([], prospectEntry);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prospectEntry);
      });

      it('should not add a ProspectEntry to an array that contains it', () => {
        const prospectEntry: IProspectEntry = sampleWithRequiredData;
        const prospectEntryCollection: IProspectEntry[] = [
          {
            ...prospectEntry,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProspectEntryToCollectionIfMissing(prospectEntryCollection, prospectEntry);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProspectEntry to an array that doesn't contain it", () => {
        const prospectEntry: IProspectEntry = sampleWithRequiredData;
        const prospectEntryCollection: IProspectEntry[] = [sampleWithPartialData];
        expectedResult = service.addProspectEntryToCollectionIfMissing(prospectEntryCollection, prospectEntry);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prospectEntry);
      });

      it('should add only unique ProspectEntry to an array', () => {
        const prospectEntryArray: IProspectEntry[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const prospectEntryCollection: IProspectEntry[] = [sampleWithRequiredData];
        expectedResult = service.addProspectEntryToCollectionIfMissing(prospectEntryCollection, ...prospectEntryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prospectEntry: IProspectEntry = sampleWithRequiredData;
        const prospectEntry2: IProspectEntry = sampleWithPartialData;
        expectedResult = service.addProspectEntryToCollectionIfMissing([], prospectEntry, prospectEntry2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prospectEntry);
        expect(expectedResult).toContain(prospectEntry2);
      });

      it('should accept null and undefined values', () => {
        const prospectEntry: IProspectEntry = sampleWithRequiredData;
        expectedResult = service.addProspectEntryToCollectionIfMissing([], null, prospectEntry, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prospectEntry);
      });

      it('should return initial array if no ProspectEntry is added', () => {
        const prospectEntryCollection: IProspectEntry[] = [sampleWithRequiredData];
        expectedResult = service.addProspectEntryToCollectionIfMissing(prospectEntryCollection, undefined, null);
        expect(expectedResult).toEqual(prospectEntryCollection);
      });
    });

    describe('compareProspectEntry', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProspectEntry(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProspectEntry(entity1, entity2);
        const compareResult2 = service.compareProspectEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProspectEntry(entity1, entity2);
        const compareResult2 = service.compareProspectEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProspectEntry(entity1, entity2);
        const compareResult2 = service.compareProspectEntry(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
