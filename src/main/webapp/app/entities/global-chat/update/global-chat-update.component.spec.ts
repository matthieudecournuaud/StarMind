import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { GlobalChatService } from '../service/global-chat.service';
import { IGlobalChat } from '../global-chat.model';
import { GlobalChatFormService } from './global-chat-form.service';

import { GlobalChatUpdateComponent } from './global-chat-update.component';

describe('GlobalChat Management Update Component', () => {
  let comp: GlobalChatUpdateComponent;
  let fixture: ComponentFixture<GlobalChatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let globalChatFormService: GlobalChatFormService;
  let globalChatService: GlobalChatService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [GlobalChatUpdateComponent],
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
      .overrideTemplate(GlobalChatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GlobalChatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    globalChatFormService = TestBed.inject(GlobalChatFormService);
    globalChatService = TestBed.inject(GlobalChatService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const globalChat: IGlobalChat = { id: 456 };
      const users: IUser[] = [{ id: 'd663c99c-667a-4ce1-be2a-b59e455c3b64' }];
      globalChat.users = users;

      const userCollection: IUser[] = [{ id: 'ed8d0a7b-c1b0-4bcb-9cbd-2d4f7085dbc8' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [...users];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ globalChat });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const globalChat: IGlobalChat = { id: 456 };
      const users: IUser = { id: 'ca63026b-b5a3-40b6-8d8a-84ef66c06f11' };
      globalChat.users = [users];

      activatedRoute.data = of({ globalChat });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(users);
      expect(comp.globalChat).toEqual(globalChat);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGlobalChat>>();
      const globalChat = { id: 123 };
      jest.spyOn(globalChatFormService, 'getGlobalChat').mockReturnValue(globalChat);
      jest.spyOn(globalChatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ globalChat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: globalChat }));
      saveSubject.complete();

      // THEN
      expect(globalChatFormService.getGlobalChat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(globalChatService.update).toHaveBeenCalledWith(expect.objectContaining(globalChat));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGlobalChat>>();
      const globalChat = { id: 123 };
      jest.spyOn(globalChatFormService, 'getGlobalChat').mockReturnValue({ id: null });
      jest.spyOn(globalChatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ globalChat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: globalChat }));
      saveSubject.complete();

      // THEN
      expect(globalChatFormService.getGlobalChat).toHaveBeenCalled();
      expect(globalChatService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGlobalChat>>();
      const globalChat = { id: 123 };
      jest.spyOn(globalChatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ globalChat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(globalChatService.update).toHaveBeenCalled();
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
