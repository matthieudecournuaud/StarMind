import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IIdeaChat } from '../idea-chat.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../idea-chat.test-samples';

import { IdeaChatService } from './idea-chat.service';

const requireRestSample: IIdeaChat = {
  ...sampleWithRequiredData,
};

describe('IdeaChat Service', () => {
  let service: IdeaChatService;
  let httpMock: HttpTestingController;
  let expectedResult: IIdeaChat | IIdeaChat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(IdeaChatService);
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

    it('should create a IdeaChat', () => {
      const ideaChat = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ideaChat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IdeaChat', () => {
      const ideaChat = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ideaChat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IdeaChat', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IdeaChat', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IdeaChat', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIdeaChatToCollectionIfMissing', () => {
      it('should add a IdeaChat to an empty array', () => {
        const ideaChat: IIdeaChat = sampleWithRequiredData;
        expectedResult = service.addIdeaChatToCollectionIfMissing([], ideaChat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ideaChat);
      });

      it('should not add a IdeaChat to an array that contains it', () => {
        const ideaChat: IIdeaChat = sampleWithRequiredData;
        const ideaChatCollection: IIdeaChat[] = [
          {
            ...ideaChat,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIdeaChatToCollectionIfMissing(ideaChatCollection, ideaChat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IdeaChat to an array that doesn't contain it", () => {
        const ideaChat: IIdeaChat = sampleWithRequiredData;
        const ideaChatCollection: IIdeaChat[] = [sampleWithPartialData];
        expectedResult = service.addIdeaChatToCollectionIfMissing(ideaChatCollection, ideaChat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ideaChat);
      });

      it('should add only unique IdeaChat to an array', () => {
        const ideaChatArray: IIdeaChat[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ideaChatCollection: IIdeaChat[] = [sampleWithRequiredData];
        expectedResult = service.addIdeaChatToCollectionIfMissing(ideaChatCollection, ...ideaChatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ideaChat: IIdeaChat = sampleWithRequiredData;
        const ideaChat2: IIdeaChat = sampleWithPartialData;
        expectedResult = service.addIdeaChatToCollectionIfMissing([], ideaChat, ideaChat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ideaChat);
        expect(expectedResult).toContain(ideaChat2);
      });

      it('should accept null and undefined values', () => {
        const ideaChat: IIdeaChat = sampleWithRequiredData;
        expectedResult = service.addIdeaChatToCollectionIfMissing([], null, ideaChat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ideaChat);
      });

      it('should return initial array if no IdeaChat is added', () => {
        const ideaChatCollection: IIdeaChat[] = [sampleWithRequiredData];
        expectedResult = service.addIdeaChatToCollectionIfMissing(ideaChatCollection, undefined, null);
        expect(expectedResult).toEqual(ideaChatCollection);
      });
    });

    describe('compareIdeaChat', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIdeaChat(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIdeaChat(entity1, entity2);
        const compareResult2 = service.compareIdeaChat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIdeaChat(entity1, entity2);
        const compareResult2 = service.compareIdeaChat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIdeaChat(entity1, entity2);
        const compareResult2 = service.compareIdeaChat(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
