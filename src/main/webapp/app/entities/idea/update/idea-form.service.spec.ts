import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../idea.test-samples';

import { IdeaFormService } from './idea-form.service';

describe('Idea Form Service', () => {
  let service: IdeaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IdeaFormService);
  });

  describe('Service methods', () => {
    describe('createIdeaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIdeaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            status: expect.any(Object),
            validation: expect.any(Object),
            rewardType: expect.any(Object),
            likes: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            author: expect.any(Object),
            ideaCategory: expect.any(Object),
            assignedReward: expect.any(Object),
            category: expect.any(Object),
            reward: expect.any(Object),
          }),
        );
      });

      it('passing IIdea should create a new form with FormGroup', () => {
        const formGroup = service.createIdeaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            status: expect.any(Object),
            validation: expect.any(Object),
            rewardType: expect.any(Object),
            likes: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            author: expect.any(Object),
            ideaCategory: expect.any(Object),
            assignedReward: expect.any(Object),
            category: expect.any(Object),
            reward: expect.any(Object),
          }),
        );
      });
    });

    describe('getIdea', () => {
      it('should return NewIdea for default Idea initial value', () => {
        const formGroup = service.createIdeaFormGroup(sampleWithNewData);

        const idea = service.getIdea(formGroup) as any;

        expect(idea).toMatchObject(sampleWithNewData);
      });

      it('should return NewIdea for empty Idea initial value', () => {
        const formGroup = service.createIdeaFormGroup();

        const idea = service.getIdea(formGroup) as any;

        expect(idea).toMatchObject({});
      });

      it('should return IIdea', () => {
        const formGroup = service.createIdeaFormGroup(sampleWithRequiredData);

        const idea = service.getIdea(formGroup) as any;

        expect(idea).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIdea should not enable id FormControl', () => {
        const formGroup = service.createIdeaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIdea should disable id FormControl', () => {
        const formGroup = service.createIdeaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
