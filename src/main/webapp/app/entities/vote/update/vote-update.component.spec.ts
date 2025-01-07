import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IIdea } from 'app/entities/idea/idea.model';
import { IdeaService } from 'app/entities/idea/service/idea.service';
import { IVote } from '../vote.model';
import { VoteService } from '../service/vote.service';
import { VoteFormService } from './vote-form.service';

import { VoteUpdateComponent } from './vote-update.component';

describe('Vote Management Update Component', () => {
  let comp: VoteUpdateComponent;
  let fixture: ComponentFixture<VoteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let voteFormService: VoteFormService;
  let voteService: VoteService;
  let userService: UserService;
  let ideaService: IdeaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VoteUpdateComponent],
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
      .overrideTemplate(VoteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VoteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    voteFormService = TestBed.inject(VoteFormService);
    voteService = TestBed.inject(VoteService);
    userService = TestBed.inject(UserService);
    ideaService = TestBed.inject(IdeaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const vote: IVote = { id: 456 };
      const voter: IUser = { id: '6d77eca6-7522-4cee-9b52-5d63e5f8d81e' };
      vote.voter = voter;

      const userCollection: IUser[] = [{ id: '03b0ba23-3d2f-43a1-a28f-77e0654bc583' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [voter];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vote });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Idea query and add missing value', () => {
      const vote: IVote = { id: 456 };
      const idea: IIdea = { id: 31497 };
      vote.idea = idea;

      const ideaCollection: IIdea[] = [{ id: 13394 }];
      jest.spyOn(ideaService, 'query').mockReturnValue(of(new HttpResponse({ body: ideaCollection })));
      const additionalIdeas = [idea];
      const expectedCollection: IIdea[] = [...additionalIdeas, ...ideaCollection];
      jest.spyOn(ideaService, 'addIdeaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vote });
      comp.ngOnInit();

      expect(ideaService.query).toHaveBeenCalled();
      expect(ideaService.addIdeaToCollectionIfMissing).toHaveBeenCalledWith(
        ideaCollection,
        ...additionalIdeas.map(expect.objectContaining),
      );
      expect(comp.ideasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vote: IVote = { id: 456 };
      const voter: IUser = { id: '1220dc39-e907-4095-b808-d6925eeb74cf' };
      vote.voter = voter;
      const idea: IIdea = { id: 29463 };
      vote.idea = idea;

      activatedRoute.data = of({ vote });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(voter);
      expect(comp.ideasSharedCollection).toContain(idea);
      expect(comp.vote).toEqual(vote);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVote>>();
      const vote = { id: 123 };
      jest.spyOn(voteFormService, 'getVote').mockReturnValue(vote);
      jest.spyOn(voteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vote }));
      saveSubject.complete();

      // THEN
      expect(voteFormService.getVote).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(voteService.update).toHaveBeenCalledWith(expect.objectContaining(vote));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVote>>();
      const vote = { id: 123 };
      jest.spyOn(voteFormService, 'getVote').mockReturnValue({ id: null });
      jest.spyOn(voteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vote: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vote }));
      saveSubject.complete();

      // THEN
      expect(voteFormService.getVote).toHaveBeenCalled();
      expect(voteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVote>>();
      const vote = { id: 123 };
      jest.spyOn(voteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(voteService.update).toHaveBeenCalled();
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
