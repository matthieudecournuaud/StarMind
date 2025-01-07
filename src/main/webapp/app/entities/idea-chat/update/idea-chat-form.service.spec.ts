import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../idea-chat.test-samples';

import { IdeaChatFormService } from './idea-chat-form.service';

describe('IdeaChat Form Service', () => {
  let service: IdeaChatFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IdeaChatFormService);
  });

  describe('Service methods', () => {
    describe('createIdeaChatFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIdeaChatFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            users: expect.any(Object),
          }),
        );
      });

      it('passing IIdeaChat should create a new form with FormGroup', () => {
        const formGroup = service.createIdeaChatFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            users: expect.any(Object),
          }),
        );
      });
    });

    describe('getIdeaChat', () => {
      it('should return NewIdeaChat for default IdeaChat initial value', () => {
        const formGroup = service.createIdeaChatFormGroup(sampleWithNewData);

        const ideaChat = service.getIdeaChat(formGroup) as any;

        expect(ideaChat).toMatchObject(sampleWithNewData);
      });

      it('should return NewIdeaChat for empty IdeaChat initial value', () => {
        const formGroup = service.createIdeaChatFormGroup();

        const ideaChat = service.getIdeaChat(formGroup) as any;

        expect(ideaChat).toMatchObject({});
      });

      it('should return IIdeaChat', () => {
        const formGroup = service.createIdeaChatFormGroup(sampleWithRequiredData);

        const ideaChat = service.getIdeaChat(formGroup) as any;

        expect(ideaChat).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIdeaChat should not enable id FormControl', () => {
        const formGroup = service.createIdeaChatFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIdeaChat should disable id FormControl', () => {
        const formGroup = service.createIdeaChatFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
