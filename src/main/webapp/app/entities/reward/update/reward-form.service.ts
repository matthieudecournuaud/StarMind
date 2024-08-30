import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IReward, NewReward } from '../reward.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReward for edit and NewRewardFormGroupInput for create.
 */
type RewardFormGroupInput = IReward | PartialWithRequiredKeyOf<NewReward>;

type RewardFormDefaults = Pick<NewReward, 'id'>;

type RewardFormGroupContent = {
  id: FormControl<IReward['id'] | NewReward['id']>;
  name: FormControl<IReward['name']>;
  description: FormControl<IReward['description']>;
};

export type RewardFormGroup = FormGroup<RewardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RewardFormService {
  createRewardFormGroup(reward: RewardFormGroupInput = { id: null }): RewardFormGroup {
    const rewardRawValue = {
      ...this.getFormDefaults(),
      ...reward,
    };
    return new FormGroup<RewardFormGroupContent>({
      id: new FormControl(
        { value: rewardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(rewardRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(rewardRawValue.description),
    });
  }

  getReward(form: RewardFormGroup): IReward | NewReward {
    return form.getRawValue() as IReward | NewReward;
  }

  resetForm(form: RewardFormGroup, reward: RewardFormGroupInput): void {
    const rewardRawValue = { ...this.getFormDefaults(), ...reward };
    form.reset(
      {
        ...rewardRawValue,
        id: { value: rewardRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RewardFormDefaults {
    return {
      id: null,
    };
  }
}
