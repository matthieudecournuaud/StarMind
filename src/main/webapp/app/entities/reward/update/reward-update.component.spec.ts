import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { RewardService } from '../service/reward.service';
import { IReward } from '../reward.model';
import { RewardFormService } from './reward-form.service';

import { RewardUpdateComponent } from './reward-update.component';

describe('Reward Management Update Component', () => {
  let comp: RewardUpdateComponent;
  let fixture: ComponentFixture<RewardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rewardFormService: RewardFormService;
  let rewardService: RewardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RewardUpdateComponent],
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
      .overrideTemplate(RewardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RewardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rewardFormService = TestBed.inject(RewardFormService);
    rewardService = TestBed.inject(RewardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const reward: IReward = { id: 456 };

      activatedRoute.data = of({ reward });
      comp.ngOnInit();

      expect(comp.reward).toEqual(reward);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReward>>();
      const reward = { id: 123 };
      jest.spyOn(rewardFormService, 'getReward').mockReturnValue(reward);
      jest.spyOn(rewardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reward });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reward }));
      saveSubject.complete();

      // THEN
      expect(rewardFormService.getReward).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rewardService.update).toHaveBeenCalledWith(expect.objectContaining(reward));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReward>>();
      const reward = { id: 123 };
      jest.spyOn(rewardFormService, 'getReward').mockReturnValue({ id: null });
      jest.spyOn(rewardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reward: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reward }));
      saveSubject.complete();

      // THEN
      expect(rewardFormService.getReward).toHaveBeenCalled();
      expect(rewardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReward>>();
      const reward = { id: 123 };
      jest.spyOn(rewardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reward });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rewardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
