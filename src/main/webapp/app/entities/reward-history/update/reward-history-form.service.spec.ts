import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../reward-history.test-samples';

import { RewardHistoryFormService } from './reward-history-form.service';

describe('RewardHistory Form Service', () => {
  let service: RewardHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RewardHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createRewardHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRewardHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            action: expect.any(Object),
            actionDate: expect.any(Object),
            description: expect.any(Object),
            reward: expect.any(Object),
            idea: expect.any(Object),
          }),
        );
      });

      it('passing IRewardHistory should create a new form with FormGroup', () => {
        const formGroup = service.createRewardHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            action: expect.any(Object),
            actionDate: expect.any(Object),
            description: expect.any(Object),
            reward: expect.any(Object),
            idea: expect.any(Object),
          }),
        );
      });
    });

    describe('getRewardHistory', () => {
      it('should return NewRewardHistory for default RewardHistory initial value', () => {
        const formGroup = service.createRewardHistoryFormGroup(sampleWithNewData);

        const rewardHistory = service.getRewardHistory(formGroup) as any;

        expect(rewardHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewRewardHistory for empty RewardHistory initial value', () => {
        const formGroup = service.createRewardHistoryFormGroup();

        const rewardHistory = service.getRewardHistory(formGroup) as any;

        expect(rewardHistory).toMatchObject({});
      });

      it('should return IRewardHistory', () => {
        const formGroup = service.createRewardHistoryFormGroup(sampleWithRequiredData);

        const rewardHistory = service.getRewardHistory(formGroup) as any;

        expect(rewardHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRewardHistory should not enable id FormControl', () => {
        const formGroup = service.createRewardHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRewardHistory should disable id FormControl', () => {
        const formGroup = service.createRewardHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
