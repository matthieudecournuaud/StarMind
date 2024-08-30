import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IIdeaHistory, NewIdeaHistory } from '../idea-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIdeaHistory for edit and NewIdeaHistoryFormGroupInput for create.
 */
type IdeaHistoryFormGroupInput = IIdeaHistory | PartialWithRequiredKeyOf<NewIdeaHistory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IIdeaHistory | NewIdeaHistory> = Omit<T, 'actionDate'> & {
  actionDate?: string | null;
};

type IdeaHistoryFormRawValue = FormValueOf<IIdeaHistory>;

type NewIdeaHistoryFormRawValue = FormValueOf<NewIdeaHistory>;

type IdeaHistoryFormDefaults = Pick<NewIdeaHistory, 'id' | 'actionDate'>;

type IdeaHistoryFormGroupContent = {
  id: FormControl<IdeaHistoryFormRawValue['id'] | NewIdeaHistory['id']>;
  action: FormControl<IdeaHistoryFormRawValue['action']>;
  actionDate: FormControl<IdeaHistoryFormRawValue['actionDate']>;
  description: FormControl<IdeaHistoryFormRawValue['description']>;
  rewardType: FormControl<IdeaHistoryFormRawValue['rewardType']>;
  likes: FormControl<IdeaHistoryFormRawValue['likes']>;
  idea: FormControl<IdeaHistoryFormRawValue['idea']>;
};

export type IdeaHistoryFormGroup = FormGroup<IdeaHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IdeaHistoryFormService {
  createIdeaHistoryFormGroup(ideaHistory: IdeaHistoryFormGroupInput = { id: null }): IdeaHistoryFormGroup {
    const ideaHistoryRawValue = this.convertIdeaHistoryToIdeaHistoryRawValue({
      ...this.getFormDefaults(),
      ...ideaHistory,
    });
    return new FormGroup<IdeaHistoryFormGroupContent>({
      id: new FormControl(
        { value: ideaHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      action: new FormControl(ideaHistoryRawValue.action, {
        validators: [Validators.required],
      }),
      actionDate: new FormControl(ideaHistoryRawValue.actionDate, {
        validators: [Validators.required],
      }),
      description: new FormControl(ideaHistoryRawValue.description),
      rewardType: new FormControl(ideaHistoryRawValue.rewardType),
      likes: new FormControl(ideaHistoryRawValue.likes),
      idea: new FormControl(ideaHistoryRawValue.idea),
    });
  }

  getIdeaHistory(form: IdeaHistoryFormGroup): IIdeaHistory | NewIdeaHistory {
    return this.convertIdeaHistoryRawValueToIdeaHistory(form.getRawValue() as IdeaHistoryFormRawValue | NewIdeaHistoryFormRawValue);
  }

  resetForm(form: IdeaHistoryFormGroup, ideaHistory: IdeaHistoryFormGroupInput): void {
    const ideaHistoryRawValue = this.convertIdeaHistoryToIdeaHistoryRawValue({ ...this.getFormDefaults(), ...ideaHistory });
    form.reset(
      {
        ...ideaHistoryRawValue,
        id: { value: ideaHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): IdeaHistoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      actionDate: currentTime,
    };
  }

  private convertIdeaHistoryRawValueToIdeaHistory(
    rawIdeaHistory: IdeaHistoryFormRawValue | NewIdeaHistoryFormRawValue,
  ): IIdeaHistory | NewIdeaHistory {
    return {
      ...rawIdeaHistory,
      actionDate: dayjs(rawIdeaHistory.actionDate, DATE_TIME_FORMAT),
    };
  }

  private convertIdeaHistoryToIdeaHistoryRawValue(
    ideaHistory: IIdeaHistory | (Partial<NewIdeaHistory> & IdeaHistoryFormDefaults),
  ): IdeaHistoryFormRawValue | PartialWithRequiredKeyOf<NewIdeaHistoryFormRawValue> {
    return {
      ...ideaHistory,
      actionDate: ideaHistory.actionDate ? ideaHistory.actionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
