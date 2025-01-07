import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVote, NewVote } from '../vote.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVote for edit and NewVoteFormGroupInput for create.
 */
type VoteFormGroupInput = IVote | PartialWithRequiredKeyOf<NewVote>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVote | NewVote> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type VoteFormRawValue = FormValueOf<IVote>;

type NewVoteFormRawValue = FormValueOf<NewVote>;

type VoteFormDefaults = Pick<NewVote, 'id' | 'createdDate'>;

type VoteFormGroupContent = {
  id: FormControl<VoteFormRawValue['id'] | NewVote['id']>;
  voteOption: FormControl<VoteFormRawValue['voteOption']>;
  comment: FormControl<VoteFormRawValue['comment']>;
  createdDate: FormControl<VoteFormRawValue['createdDate']>;
  voter: FormControl<VoteFormRawValue['voter']>;
  idea: FormControl<VoteFormRawValue['idea']>;
};

export type VoteFormGroup = FormGroup<VoteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VoteFormService {
  createVoteFormGroup(vote: VoteFormGroupInput = { id: null }): VoteFormGroup {
    const voteRawValue = this.convertVoteToVoteRawValue({
      ...this.getFormDefaults(),
      ...vote,
    });
    return new FormGroup<VoteFormGroupContent>({
      id: new FormControl(
        { value: voteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      voteOption: new FormControl(voteRawValue.voteOption, {
        validators: [Validators.required],
      }),
      comment: new FormControl(voteRawValue.comment),
      createdDate: new FormControl(voteRawValue.createdDate, {
        validators: [Validators.required],
      }),
      voter: new FormControl(voteRawValue.voter),
      idea: new FormControl(voteRawValue.idea),
    });
  }

  getVote(form: VoteFormGroup): IVote | NewVote {
    return this.convertVoteRawValueToVote(form.getRawValue() as VoteFormRawValue | NewVoteFormRawValue);
  }

  resetForm(form: VoteFormGroup, vote: VoteFormGroupInput): void {
    const voteRawValue = this.convertVoteToVoteRawValue({ ...this.getFormDefaults(), ...vote });
    form.reset(
      {
        ...voteRawValue,
        id: { value: voteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VoteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertVoteRawValueToVote(rawVote: VoteFormRawValue | NewVoteFormRawValue): IVote | NewVote {
    return {
      ...rawVote,
      createdDate: dayjs(rawVote.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertVoteToVoteRawValue(
    vote: IVote | (Partial<NewVote> & VoteFormDefaults),
  ): VoteFormRawValue | PartialWithRequiredKeyOf<NewVoteFormRawValue> {
    return {
      ...vote,
      createdDate: vote.createdDate ? vote.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
