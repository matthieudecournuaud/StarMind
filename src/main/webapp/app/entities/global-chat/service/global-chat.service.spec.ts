import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IGlobalChat } from '../global-chat.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../global-chat.test-samples';

import { GlobalChatService } from './global-chat.service';

const requireRestSample: IGlobalChat = {
  ...sampleWithRequiredData,
};

describe('GlobalChat Service', () => {
  let service: GlobalChatService;
  let httpMock: HttpTestingController;
  let expectedResult: IGlobalChat | IGlobalChat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(GlobalChatService);
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

    it('should create a GlobalChat', () => {
      const globalChat = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(globalChat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GlobalChat', () => {
      const globalChat = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(globalChat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GlobalChat', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GlobalChat', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GlobalChat', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGlobalChatToCollectionIfMissing', () => {
      it('should add a GlobalChat to an empty array', () => {
        const globalChat: IGlobalChat = sampleWithRequiredData;
        expectedResult = service.addGlobalChatToCollectionIfMissing([], globalChat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(globalChat);
      });

      it('should not add a GlobalChat to an array that contains it', () => {
        const globalChat: IGlobalChat = sampleWithRequiredData;
        const globalChatCollection: IGlobalChat[] = [
          {
            ...globalChat,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGlobalChatToCollectionIfMissing(globalChatCollection, globalChat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GlobalChat to an array that doesn't contain it", () => {
        const globalChat: IGlobalChat = sampleWithRequiredData;
        const globalChatCollection: IGlobalChat[] = [sampleWithPartialData];
        expectedResult = service.addGlobalChatToCollectionIfMissing(globalChatCollection, globalChat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(globalChat);
      });

      it('should add only unique GlobalChat to an array', () => {
        const globalChatArray: IGlobalChat[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const globalChatCollection: IGlobalChat[] = [sampleWithRequiredData];
        expectedResult = service.addGlobalChatToCollectionIfMissing(globalChatCollection, ...globalChatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const globalChat: IGlobalChat = sampleWithRequiredData;
        const globalChat2: IGlobalChat = sampleWithPartialData;
        expectedResult = service.addGlobalChatToCollectionIfMissing([], globalChat, globalChat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(globalChat);
        expect(expectedResult).toContain(globalChat2);
      });

      it('should accept null and undefined values', () => {
        const globalChat: IGlobalChat = sampleWithRequiredData;
        expectedResult = service.addGlobalChatToCollectionIfMissing([], null, globalChat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(globalChat);
      });

      it('should return initial array if no GlobalChat is added', () => {
        const globalChatCollection: IGlobalChat[] = [sampleWithRequiredData];
        expectedResult = service.addGlobalChatToCollectionIfMissing(globalChatCollection, undefined, null);
        expect(expectedResult).toEqual(globalChatCollection);
      });
    });

    describe('compareGlobalChat', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGlobalChat(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGlobalChat(entity1, entity2);
        const compareResult2 = service.compareGlobalChat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGlobalChat(entity1, entity2);
        const compareResult2 = service.compareGlobalChat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGlobalChat(entity1, entity2);
        const compareResult2 = service.compareGlobalChat(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
