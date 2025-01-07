import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IdeaChatService } from '../service/idea-chat.service';
import { IIdeaChat } from '../idea-chat.model';
import { IdeaChatFormService } from './idea-chat-form.service';

import { IdeaChatUpdateComponent } from './idea-chat-update.component';

describe('IdeaChat Management Update Component', () => {
  let comp: IdeaChatUpdateComponent;
  let fixture: ComponentFixture<IdeaChatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ideaChatFormService: IdeaChatFormService;
  let ideaChatService: IdeaChatService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [IdeaChatUpdateComponent],
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
      .overrideTemplate(IdeaChatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IdeaChatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ideaChatFormService = TestBed.inject(IdeaChatFormService);
    ideaChatService = TestBed.inject(IdeaChatService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const ideaChat: IIdeaChat = { id: 456 };
      const users: IUser[] = [{ id: '416884b1-096f-4ec4-a34a-cc1f81e790fb' }];
      ideaChat.users = users;

      const userCollection: IUser[] = [{ id: '3fb7278d-ae0b-4d3c-86ba-69f99ff74d75' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [...users];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ideaChat });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ideaChat: IIdeaChat = { id: 456 };
      const users: IUser = { id: 'ff5ae2f8-3d59-4c85-9d2d-83450f035457' };
      ideaChat.users = [users];

      activatedRoute.data = of({ ideaChat });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(users);
      expect(comp.ideaChat).toEqual(ideaChat);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIdeaChat>>();
      const ideaChat = { id: 123 };
      jest.spyOn(ideaChatFormService, 'getIdeaChat').mockReturnValue(ideaChat);
      jest.spyOn(ideaChatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ideaChat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ideaChat }));
      saveSubject.complete();

      // THEN
      expect(ideaChatFormService.getIdeaChat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ideaChatService.update).toHaveBeenCalledWith(expect.objectContaining(ideaChat));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIdeaChat>>();
      const ideaChat = { id: 123 };
      jest.spyOn(ideaChatFormService, 'getIdeaChat').mockReturnValue({ id: null });
      jest.spyOn(ideaChatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ideaChat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ideaChat }));
      saveSubject.complete();

      // THEN
      expect(ideaChatFormService.getIdeaChat).toHaveBeenCalled();
      expect(ideaChatService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIdeaChat>>();
      const ideaChat = { id: 123 };
      jest.spyOn(ideaChatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ideaChat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ideaChatService.update).toHaveBeenCalled();
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
  });
});
