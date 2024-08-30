import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../like-history.test-samples';

import { LikeHistoryFormService } from './like-history-form.service';

describe('LikeHistory Form Service', () => {
  let service: LikeHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LikeHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createLikeHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLikeHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            action: expect.any(Object),
            actionDate: expect.any(Object),
            oldLikes: expect.any(Object),
            newLikes: expect.any(Object),
            idea: expect.any(Object),
          }),
        );
      });

      it('passing ILikeHistory should create a new form with FormGroup', () => {
        const formGroup = service.createLikeHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            action: expect.any(Object),
            actionDate: expect.any(Object),
            oldLikes: expect.any(Object),
            newLikes: expect.any(Object),
            idea: expect.any(Object),
          }),
        );
      });
    });

    describe('getLikeHistory', () => {
      it('should return NewLikeHistory for default LikeHistory initial value', () => {
        const formGroup = service.createLikeHistoryFormGroup(sampleWithNewData);

        const likeHistory = service.getLikeHistory(formGroup) as any;

        expect(likeHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewLikeHistory for empty LikeHistory initial value', () => {
        const formGroup = service.createLikeHistoryFormGroup();

        const likeHistory = service.getLikeHistory(formGroup) as any;

        expect(likeHistory).toMatchObject({});
      });

      it('should return ILikeHistory', () => {
        const formGroup = service.createLikeHistoryFormGroup(sampleWithRequiredData);

        const likeHistory = service.getLikeHistory(formGroup) as any;

        expect(likeHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILikeHistory should not enable id FormControl', () => {
        const formGroup = service.createLikeHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLikeHistory should disable id FormControl', () => {
        const formGroup = service.createLikeHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
