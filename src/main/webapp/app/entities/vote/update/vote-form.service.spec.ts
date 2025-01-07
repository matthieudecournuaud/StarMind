import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../vote.test-samples';

import { VoteFormService } from './vote-form.service';

describe('Vote Form Service', () => {
  let service: VoteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VoteFormService);
  });

  describe('Service methods', () => {
    describe('createVoteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVoteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            voteOption: expect.any(Object),
            comment: expect.any(Object),
            createdDate: expect.any(Object),
            voter: expect.any(Object),
            idea: expect.any(Object),
          }),
        );
      });

      it('passing IVote should create a new form with FormGroup', () => {
        const formGroup = service.createVoteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            voteOption: expect.any(Object),
            comment: expect.any(Object),
            createdDate: expect.any(Object),
            voter: expect.any(Object),
            idea: expect.any(Object),
          }),
        );
      });
    });

    describe('getVote', () => {
      it('should return NewVote for default Vote initial value', () => {
        const formGroup = service.createVoteFormGroup(sampleWithNewData);

        const vote = service.getVote(formGroup) as any;

        expect(vote).toMatchObject(sampleWithNewData);
      });

      it('should return NewVote for empty Vote initial value', () => {
        const formGroup = service.createVoteFormGroup();

        const vote = service.getVote(formGroup) as any;

        expect(vote).toMatchObject({});
      });

      it('should return IVote', () => {
        const formGroup = service.createVoteFormGroup(sampleWithRequiredData);

        const vote = service.getVote(formGroup) as any;

        expect(vote).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVote should not enable id FormControl', () => {
        const formGroup = service.createVoteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVote should disable id FormControl', () => {
        const formGroup = service.createVoteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
