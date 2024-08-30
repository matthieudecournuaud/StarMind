import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IReward } from 'app/entities/reward/reward.model';
import { RewardService } from 'app/entities/reward/service/reward.service';
import { IIdea } from 'app/entities/idea/idea.model';
import { IdeaService } from 'app/entities/idea/service/idea.service';
import { IRewardHistory } from '../reward-history.model';
import { RewardHistoryService } from '../service/reward-history.service';
import { RewardHistoryFormService } from './reward-history-form.service';

import { RewardHistoryUpdateComponent } from './reward-history-update.component';

describe('RewardHistory Management Update Component', () => {
  let comp: RewardHistoryUpdateComponent;
  let fixture: ComponentFixture<RewardHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rewardHistoryFormService: RewardHistoryFormService;
  let rewardHistoryService: RewardHistoryService;
  let rewardService: RewardService;
  let ideaService: IdeaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RewardHistoryUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RewardHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RewardHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rewardHistoryFormService = TestBed.inject(RewardHistoryFormService);
    rewardHistoryService = TestBed.inject(RewardHistoryService);
    rewardService = TestBed.inject(RewardService);
    ideaService = TestBed.inject(IdeaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Reward query and add missing value', () => {
      const rewardHistory: IRewardHistory = { id: 456 };
      const reward: IReward = { id: 13743 };
      rewardHistory.reward = reward;

      const rewardCollection: IReward[] = [{ id: 7676 }];
      jest.spyOn(rewardService, 'query').mockReturnValue(of(new HttpResponse({ body: rewardCollection })));
      const additionalRewards = [reward];
      const expectedCollection: IReward[] = [...additionalRewards, ...rewardCollection];
      jest.spyOn(rewardService, 'addRewardToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rewardHistory });
      comp.ngOnInit();

      expect(rewardService.query).toHaveBeenCalled();
      expect(rewardService.addRewardToCollectionIfMissing).toHaveBeenCalledWith(
        rewardCollection,
        ...additionalRewards.map(expect.objectContaining),
      );
      expect(comp.rewardsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Idea query and add missing value', () => {
      const rewardHistory: IRewardHistory = { id: 456 };
      const idea: IIdea = { id: 10446 };
      rewardHistory.idea = idea;

      const ideaCollection: IIdea[] = [{ id: 29748 }];
      jest.spyOn(ideaService, 'query').mockReturnValue(of(new HttpResponse({ body: ideaCollection })));
      const additionalIdeas = [idea];
      const expectedCollection: IIdea[] = [...additionalIdeas, ...ideaCollection];
      jest.spyOn(ideaService, 'addIdeaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rewardHistory });
      comp.ngOnInit();

      expect(ideaService.query).toHaveBeenCalled();
      expect(ideaService.addIdeaToCollectionIfMissing).toHaveBeenCalledWith(
        ideaCollection,
        ...additionalIdeas.map(expect.objectContaining),
      );
      expect(comp.ideasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rewardHistory: IRewardHistory = { id: 456 };
      const reward: IReward = { id: 17038 };
      rewardHistory.reward = reward;
      const idea: IIdea = { id: 18173 };
      rewardHistory.idea = idea;

      activatedRoute.data = of({ rewardHistory });
      comp.ngOnInit();

      expect(comp.rewardsSharedCollection).toContain(reward);
      expect(comp.ideasSharedCollection).toContain(idea);
      expect(comp.rewardHistory).toEqual(rewardHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRewardHistory>>();
      const rewardHistory = { id: 123 };
      jest.spyOn(rewardHistoryFormService, 'getRewardHistory').mockReturnValue(rewardHistory);
      jest.spyOn(rewardHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rewardHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rewardHistory }));
      saveSubject.complete();

      // THEN
      expect(rewardHistoryFormService.getRewardHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rewardHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(rewardHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRewardHistory>>();
      const rewardHistory = { id: 123 };
      jest.spyOn(rewardHistoryFormService, 'getRewardHistory').mockReturnValue({ id: null });
      jest.spyOn(rewardHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rewardHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rewardHistory }));
      saveSubject.complete();

      // THEN
      expect(rewardHistoryFormService.getRewardHistory).toHaveBeenCalled();
      expect(rewardHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRewardHistory>>();
      const rewardHistory = { id: 123 };
      jest.spyOn(rewardHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rewardHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rewardHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReward', () => {
      it('Should forward to rewardService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(rewardService, 'compareReward');
        comp.compareReward(entity, entity2);
        expect(rewardService.compareReward).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareIdea', () => {
      it('Should forward to ideaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ideaService, 'compareIdea');
        comp.compareIdea(entity, entity2);
        expect(ideaService.compareIdea).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
