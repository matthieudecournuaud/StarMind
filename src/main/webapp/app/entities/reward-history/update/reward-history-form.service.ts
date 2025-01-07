import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRewardHistory, NewRewardHistory } from '../reward-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRewardHistory for edit and NewRewardHistoryFormGroupInput for create.
 */
type RewardHistoryFormGroupInput = IRewardHistory | PartialWithRequiredKeyOf<NewRewardHistory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRewardHistory | NewRewardHistory> = Omit<T, 'actionDate'> & {
  actionDate?: string | null;
};

type RewardHistoryFormRawValue = FormValueOf<IRewardHistory>;

type NewRewardHistoryFormRawValue = FormValueOf<NewRewardHistory>;

type RewardHistoryFormDefaults = Pick<NewRewardHistory, 'id' | 'actionDate'>;

type RewardHistoryFormGroupContent = {
  id: FormControl<RewardHistoryFormRawValue['id'] | NewRewardHistory['id']>;
  action: FormControl<RewardHistoryFormRawValue['action']>;
  actionDate: FormControl<RewardHistoryFormRawValue['actionDate']>;
  description: FormControl<RewardHistoryFormRawValue['description']>;
  idea: FormControl<RewardHistoryFormRawValue['idea']>;
  reward: FormControl<RewardHistoryFormRawValue['reward']>;
};

export type RewardHistoryFormGroup = FormGroup<RewardHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RewardHistoryFormService {
  createRewardHistoryFormGroup(rewardHistory: RewardHistoryFormGroupInput = { id: null }): RewardHistoryFormGroup {
    const rewardHistoryRawValue = this.convertRewardHistoryToRewardHistoryRawValue({
      ...this.getFormDefaults(),
      ...rewardHistory,
    });
    return new FormGroup<RewardHistoryFormGroupContent>({
      id: new FormControl(
        { value: rewardHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      action: new FormControl(rewardHistoryRawValue.action, {
        validators: [Validators.required],
      }),
      actionDate: new FormControl(rewardHistoryRawValue.actionDate, {
        validators: [Validators.required],
      }),
      description: new FormControl(rewardHistoryRawValue.description),
      idea: new FormControl(rewardHistoryRawValue.idea),
      reward: new FormControl(rewardHistoryRawValue.reward),
    });
  }

  getRewardHistory(form: RewardHistoryFormGroup): IRewardHistory | NewRewardHistory {
    return this.convertRewardHistoryRawValueToRewardHistory(form.getRawValue() as RewardHistoryFormRawValue | NewRewardHistoryFormRawValue);
  }

  resetForm(form: RewardHistoryFormGroup, rewardHistory: RewardHistoryFormGroupInput): void {
    const rewardHistoryRawValue = this.convertRewardHistoryToRewardHistoryRawValue({ ...this.getFormDefaults(), ...rewardHistory });
    form.reset(
      {
        ...rewardHistoryRawValue,
        id: { value: rewardHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RewardHistoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      actionDate: currentTime,
    };
  }

  private convertRewardHistoryRawValueToRewardHistory(
    rawRewardHistory: RewardHistoryFormRawValue | NewRewardHistoryFormRawValue,
  ): IRewardHistory | NewRewardHistory {
    return {
      ...rawRewardHistory,
      actionDate: dayjs(rawRewardHistory.actionDate, DATE_TIME_FORMAT),
    };
  }

  private convertRewardHistoryToRewardHistoryRawValue(
    rewardHistory: IRewardHistory | (Partial<NewRewardHistory> & RewardHistoryFormDefaults),
  ): RewardHistoryFormRawValue | PartialWithRequiredKeyOf<NewRewardHistoryFormRawValue> {
    return {
      ...rewardHistory,
      actionDate: rewardHistory.actionDate ? rewardHistory.actionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
