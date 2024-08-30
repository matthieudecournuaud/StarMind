import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IReward } from 'app/entities/reward/reward.model';
import { RewardService } from 'app/entities/reward/service/reward.service';
import { IIdea } from '../idea.model';
import { IdeaService } from '../service/idea.service';
import { IdeaFormService } from './idea-form.service';

import { IdeaUpdateComponent } from './idea-update.component';

describe('Idea Management Update Component', () => {
  let comp: IdeaUpdateComponent;
  let fixture: ComponentFixture<IdeaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ideaFormService: IdeaFormService;
  let ideaService: IdeaService;
  let userService: UserService;
  let categoryService: CategoryService;
  let rewardService: RewardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [IdeaUpdateComponent],
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
      .overrideTemplate(IdeaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IdeaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ideaFormService = TestBed.inject(IdeaFormService);
    ideaService = TestBed.inject(IdeaService);
    userService = TestBed.inject(UserService);
    categoryService = TestBed.inject(CategoryService);
    rewardService = TestBed.inject(RewardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const idea: IIdea = { id: 456 };
      const author: IUser = { id: 'a7f8882c-3f3b-43ce-bf08-b044e7939f90' };
      idea.author = author;

      const userCollection: IUser[] = [{ id: '0555c1d8-523c-4341-a1b1-b652e213371b' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [author];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ idea });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Category query and add missing value', () => {
      const idea: IIdea = { id: 456 };
      const ideaCategory: ICategory = { id: 29988 };
      idea.ideaCategory = ideaCategory;
      const category: ICategory = { id: 20958 };
      idea.category = category;

      const categoryCollection: ICategory[] = [{ id: 11100 }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [ideaCategory, category];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ idea });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        categoryCollection,
        ...additionalCategories.map(expect.objectContaining),
      );
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Reward query and add missing value', () => {
      const idea: IIdea = { id: 456 };
      const assignedReward: IReward = { id: 21274 };
      idea.assignedReward = assignedReward;
      const reward: IReward = { id: 3241 };
      idea.reward = reward;

      const rewardCollection: IReward[] = [{ id: 15109 }];
      jest.spyOn(rewardService, 'query').mockReturnValue(of(new HttpResponse({ body: rewardCollection })));
      const additionalRewards = [assignedReward, reward];
      const expectedCollection: IReward[] = [...additionalRewards, ...rewardCollection];
      jest.spyOn(rewardService, 'addRewardToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ idea });
      comp.ngOnInit();

      expect(rewardService.query).toHaveBeenCalled();
      expect(rewardService.addRewardToCollectionIfMissing).toHaveBeenCalledWith(
        rewardCollection,
        ...additionalRewards.map(expect.objectContaining),
      );
      expect(comp.rewardsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const idea: IIdea = { id: 456 };
      const author: IUser = { id: 'f50be526-8e59-4b37-86ed-80f4c30b1509' };
      idea.author = author;
      const ideaCategory: ICategory = { id: 23374 };
      idea.ideaCategory = ideaCategory;
      const category: ICategory = { id: 16414 };
      idea.category = category;
      const assignedReward: IReward = { id: 1486 };
      idea.assignedReward = assignedReward;
      const reward: IReward = { id: 19379 };
      idea.reward = reward;

      activatedRoute.data = of({ idea });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(author);
      expect(comp.categoriesSharedCollection).toContain(ideaCategory);
      expect(comp.categoriesSharedCollection).toContain(category);
      expect(comp.rewardsSharedCollection).toContain(assignedReward);
      expect(comp.rewardsSharedCollection).toContain(reward);
      expect(comp.idea).toEqual(idea);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIdea>>();
      const idea = { id: 123 };
      jest.spyOn(ideaFormService, 'getIdea').mockReturnValue(idea);
      jest.spyOn(ideaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ idea });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: idea }));
      saveSubject.complete();

      // THEN
      expect(ideaFormService.getIdea).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ideaService.update).toHaveBeenCalledWith(expect.objectContaining(idea));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIdea>>();
      const idea = { id: 123 };
      jest.spyOn(ideaFormService, 'getIdea').mockReturnValue({ id: null });
      jest.spyOn(ideaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ idea: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: idea }));
      saveSubject.complete();

      // THEN
      expect(ideaFormService.getIdea).toHaveBeenCalled();
      expect(ideaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIdea>>();
      const idea = { id: 123 };
      jest.spyOn(ideaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ idea });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ideaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCategory', () => {
      it('Should forward to categoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categoryService, 'compareCategory');
        comp.compareCategory(entity, entity2);
        expect(categoryService.compareCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareReward', () => {
      it('Should forward to rewardService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(rewardService, 'compareReward');
        comp.compareReward(entity, entity2);
        expect(rewardService.compareReward).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
