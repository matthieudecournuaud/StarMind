import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../reward.test-samples';

import { RewardFormService } from './reward-form.service';

describe('Reward Form Service', () => {
  let service: RewardFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RewardFormService);
  });

  describe('Service methods', () => {
    describe('createRewardFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRewardFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IReward should create a new form with FormGroup', () => {
        const formGroup = service.createRewardFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getReward', () => {
      it('should return NewReward for default Reward initial value', () => {
        const formGroup = service.createRewardFormGroup(sampleWithNewData);

        const reward = service.getReward(formGroup) as any;

        expect(reward).toMatchObject(sampleWithNewData);
      });

      it('should return NewReward for empty Reward initial value', () => {
        const formGroup = service.createRewardFormGroup();

        const reward = service.getReward(formGroup) as any;

        expect(reward).toMatchObject({});
      });

      it('should return IReward', () => {
        const formGroup = service.createRewardFormGroup(sampleWithRequiredData);

        const reward = service.getReward(formGroup) as any;

        expect(reward).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReward should not enable id FormControl', () => {
        const formGroup = service.createRewardFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReward should disable id FormControl', () => {
        const formGroup = service.createRewardFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
