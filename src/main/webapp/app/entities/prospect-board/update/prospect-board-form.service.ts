import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProspectBoard, NewProspectBoard } from '../prospect-board.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProspectBoard for edit and NewProspectBoardFormGroupInput for create.
 */
type ProspectBoardFormGroupInput = IProspectBoard | PartialWithRequiredKeyOf<NewProspectBoard>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProspectBoard | NewProspectBoard> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type ProspectBoardFormRawValue = FormValueOf<IProspectBoard>;

type NewProspectBoardFormRawValue = FormValueOf<NewProspectBoard>;

type ProspectBoardFormDefaults = Pick<NewProspectBoard, 'id' | 'createdDate' | 'modifiedDate'>;

type ProspectBoardFormGroupContent = {
  id: FormControl<ProspectBoardFormRawValue['id'] | NewProspectBoard['id']>;
  name: FormControl<ProspectBoardFormRawValue['name']>;
  description: FormControl<ProspectBoardFormRawValue['description']>;
  createdDate: FormControl<ProspectBoardFormRawValue['createdDate']>;
  modifiedDate: FormControl<ProspectBoardFormRawValue['modifiedDate']>;
  owner: FormControl<ProspectBoardFormRawValue['owner']>;
};

export type ProspectBoardFormGroup = FormGroup<ProspectBoardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProspectBoardFormService {
  createProspectBoardFormGroup(prospectBoard: ProspectBoardFormGroupInput = { id: null }): ProspectBoardFormGroup {
    const prospectBoardRawValue = this.convertProspectBoardToProspectBoardRawValue({
      ...this.getFormDefaults(),
      ...prospectBoard,
    });
    return new FormGroup<ProspectBoardFormGroupContent>({
      id: new FormControl(
        { value: prospectBoardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(prospectBoardRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(prospectBoardRawValue.description),
      createdDate: new FormControl(prospectBoardRawValue.createdDate, {
        validators: [Validators.required],
      }),
      modifiedDate: new FormControl(prospectBoardRawValue.modifiedDate, {
        validators: [Validators.required],
      }),
      owner: new FormControl(prospectBoardRawValue.owner),
    });
  }

  getProspectBoard(form: ProspectBoardFormGroup): IProspectBoard | NewProspectBoard {
    return this.convertProspectBoardRawValueToProspectBoard(form.getRawValue() as ProspectBoardFormRawValue | NewProspectBoardFormRawValue);
  }

  resetForm(form: ProspectBoardFormGroup, prospectBoard: ProspectBoardFormGroupInput): void {
    const prospectBoardRawValue = this.convertProspectBoardToProspectBoardRawValue({ ...this.getFormDefaults(), ...prospectBoard });
    form.reset(
      {
        ...prospectBoardRawValue,
        id: { value: prospectBoardRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProspectBoardFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      modifiedDate: currentTime,
    };
  }

  private convertProspectBoardRawValueToProspectBoard(
    rawProspectBoard: ProspectBoardFormRawValue | NewProspectBoardFormRawValue,
  ): IProspectBoard | NewProspectBoard {
    return {
      ...rawProspectBoard,
      createdDate: dayjs(rawProspectBoard.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawProspectBoard.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertProspectBoardToProspectBoardRawValue(
    prospectBoard: IProspectBoard | (Partial<NewProspectBoard> & ProspectBoardFormDefaults),
  ): ProspectBoardFormRawValue | PartialWithRequiredKeyOf<NewProspectBoardFormRawValue> {
    return {
      ...prospectBoard,
      createdDate: prospectBoard.createdDate ? prospectBoard.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: prospectBoard.modifiedDate ? prospectBoard.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
