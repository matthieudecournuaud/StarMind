import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../idea-history.test-samples';

import { IdeaHistoryFormService } from './idea-history-form.service';

describe('IdeaHistory Form Service', () => {
  let service: IdeaHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IdeaHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createIdeaHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIdeaHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            action: expect.any(Object),
            actionDate: expect.any(Object),
            description: expect.any(Object),
            rewardType: expect.any(Object),
            likes: expect.any(Object),
            idea: expect.any(Object),
          }),
        );
      });

      it('passing IIdeaHistory should create a new form with FormGroup', () => {
        const formGroup = service.createIdeaHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            action: expect.any(Object),
            actionDate: expect.any(Object),
            description: expect.any(Object),
            rewardType: expect.any(Object),
            likes: expect.any(Object),
            idea: expect.any(Object),
          }),
        );
      });
    });

    describe('getIdeaHistory', () => {
      it('should return NewIdeaHistory for default IdeaHistory initial value', () => {
        const formGroup = service.createIdeaHistoryFormGroup(sampleWithNewData);

        const ideaHistory = service.getIdeaHistory(formGroup) as any;

        expect(ideaHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewIdeaHistory for empty IdeaHistory initial value', () => {
        const formGroup = service.createIdeaHistoryFormGroup();

        const ideaHistory = service.getIdeaHistory(formGroup) as any;

        expect(ideaHistory).toMatchObject({});
      });

      it('should return IIdeaHistory', () => {
        const formGroup = service.createIdeaHistoryFormGroup(sampleWithRequiredData);

        const ideaHistory = service.getIdeaHistory(formGroup) as any;

        expect(ideaHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIdeaHistory should not enable id FormControl', () => {
        const formGroup = service.createIdeaHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIdeaHistory should disable id FormControl', () => {
        const formGroup = service.createIdeaHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
