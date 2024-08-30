import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILikeHistory, NewLikeHistory } from '../like-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILikeHistory for edit and NewLikeHistoryFormGroupInput for create.
 */
type LikeHistoryFormGroupInput = ILikeHistory | PartialWithRequiredKeyOf<NewLikeHistory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILikeHistory | NewLikeHistory> = Omit<T, 'actionDate'> & {
  actionDate?: string | null;
};

type LikeHistoryFormRawValue = FormValueOf<ILikeHistory>;

type NewLikeHistoryFormRawValue = FormValueOf<NewLikeHistory>;

type LikeHistoryFormDefaults = Pick<NewLikeHistory, 'id' | 'actionDate'>;

type LikeHistoryFormGroupContent = {
  id: FormControl<LikeHistoryFormRawValue['id'] | NewLikeHistory['id']>;
  action: FormControl<LikeHistoryFormRawValue['action']>;
  actionDate: FormControl<LikeHistoryFormRawValue['actionDate']>;
  oldLikes: FormControl<LikeHistoryFormRawValue['oldLikes']>;
  newLikes: FormControl<LikeHistoryFormRawValue['newLikes']>;
  idea: FormControl<LikeHistoryFormRawValue['idea']>;
};

export type LikeHistoryFormGroup = FormGroup<LikeHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LikeHistoryFormService {
  createLikeHistoryFormGroup(likeHistory: LikeHistoryFormGroupInput = { id: null }): LikeHistoryFormGroup {
    const likeHistoryRawValue = this.convertLikeHistoryToLikeHistoryRawValue({
      ...this.getFormDefaults(),
      ...likeHistory,
    });
    return new FormGroup<LikeHistoryFormGroupContent>({
      id: new FormControl(
        { value: likeHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      action: new FormControl(likeHistoryRawValue.action, {
        validators: [Validators.required],
      }),
      actionDate: new FormControl(likeHistoryRawValue.actionDate, {
        validators: [Validators.required],
      }),
      oldLikes: new FormControl(likeHistoryRawValue.oldLikes),
      newLikes: new FormControl(likeHistoryRawValue.newLikes),
      idea: new FormControl(likeHistoryRawValue.idea),
    });
  }

  getLikeHistory(form: LikeHistoryFormGroup): ILikeHistory | NewLikeHistory {
    return this.convertLikeHistoryRawValueToLikeHistory(form.getRawValue() as LikeHistoryFormRawValue | NewLikeHistoryFormRawValue);
  }

  resetForm(form: LikeHistoryFormGroup, likeHistory: LikeHistoryFormGroupInput): void {
    const likeHistoryRawValue = this.convertLikeHistoryToLikeHistoryRawValue({ ...this.getFormDefaults(), ...likeHistory });
    form.reset(
      {
        ...likeHistoryRawValue,
        id: { value: likeHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LikeHistoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      actionDate: currentTime,
    };
  }

  private convertLikeHistoryRawValueToLikeHistory(
    rawLikeHistory: LikeHistoryFormRawValue | NewLikeHistoryFormRawValue,
  ): ILikeHistory | NewLikeHistory {
    return {
      ...rawLikeHistory,
      actionDate: dayjs(rawLikeHistory.actionDate, DATE_TIME_FORMAT),
    };
  }

  private convertLikeHistoryToLikeHistoryRawValue(
    likeHistory: ILikeHistory | (Partial<NewLikeHistory> & LikeHistoryFormDefaults),
  ): LikeHistoryFormRawValue | PartialWithRequiredKeyOf<NewLikeHistoryFormRawValue> {
    return {
      ...likeHistory,
      actionDate: likeHistory.actionDate ? likeHistory.actionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
