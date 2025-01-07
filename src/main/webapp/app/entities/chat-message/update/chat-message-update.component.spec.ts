import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IGlobalChat } from 'app/entities/global-chat/global-chat.model';
import { GlobalChatService } from 'app/entities/global-chat/service/global-chat.service';
import { IIdeaChat } from 'app/entities/idea-chat/idea-chat.model';
import { IdeaChatService } from 'app/entities/idea-chat/service/idea-chat.service';
import { IChatMessage } from '../chat-message.model';
import { ChatMessageService } from '../service/chat-message.service';
import { ChatMessageFormService } from './chat-message-form.service';

import { ChatMessageUpdateComponent } from './chat-message-update.component';

describe('ChatMessage Management Update Component', () => {
  let comp: ChatMessageUpdateComponent;
  let fixture: ComponentFixture<ChatMessageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let chatMessageFormService: ChatMessageFormService;
  let chatMessageService: ChatMessageService;
  let userService: UserService;
  let globalChatService: GlobalChatService;
  let ideaChatService: IdeaChatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ChatMessageUpdateComponent],
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
      .overrideTemplate(ChatMessageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChatMessageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    chatMessageFormService = TestBed.inject(ChatMessageFormService);
    chatMessageService = TestBed.inject(ChatMessageService);
    userService = TestBed.inject(UserService);
    globalChatService = TestBed.inject(GlobalChatService);
    ideaChatService = TestBed.inject(IdeaChatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const chatMessage: IChatMessage = { id: 456 };
      const author: IUser = { id: 'be046d03-0169-4737-8561-168a6e214ae0' };
      chatMessage.author = author;

      const userCollection: IUser[] = [{ id: '8b186e46-887f-4189-8cca-72991f1c09aa' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [author];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ chatMessage });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call GlobalChat query and add missing value', () => {
      const chatMessage: IChatMessage = { id: 456 };
      const globalChat: IGlobalChat = { id: 14226 };
      chatMessage.globalChat = globalChat;

      const globalChatCollection: IGlobalChat[] = [{ id: 7419 }];
      jest.spyOn(globalChatService, 'query').mockReturnValue(of(new HttpResponse({ body: globalChatCollection })));
      const additionalGlobalChats = [globalChat];
      const expectedCollection: IGlobalChat[] = [...additionalGlobalChats, ...globalChatCollection];
      jest.spyOn(globalChatService, 'addGlobalChatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ chatMessage });
      comp.ngOnInit();

      expect(globalChatService.query).toHaveBeenCalled();
      expect(globalChatService.addGlobalChatToCollectionIfMissing).toHaveBeenCalledWith(
        globalChatCollection,
        ...additionalGlobalChats.map(expect.objectContaining),
      );
      expect(comp.globalChatsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call IdeaChat query and add missing value', () => {
      const chatMessage: IChatMessage = { id: 456 };
      const ideaChat: IIdeaChat = { id: 30611 };
      chatMessage.ideaChat = ideaChat;

      const ideaChatCollection: IIdeaChat[] = [{ id: 739 }];
      jest.spyOn(ideaChatService, 'query').mockReturnValue(of(new HttpResponse({ body: ideaChatCollection })));
      const additionalIdeaChats = [ideaChat];
      const expectedCollection: IIdeaChat[] = [...additionalIdeaChats, ...ideaChatCollection];
      jest.spyOn(ideaChatService, 'addIdeaChatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ chatMessage });
      comp.ngOnInit();

      expect(ideaChatService.query).toHaveBeenCalled();
      expect(ideaChatService.addIdeaChatToCollectionIfMissing).toHaveBeenCalledWith(
        ideaChatCollection,
        ...additionalIdeaChats.map(expect.objectContaining),
      );
      expect(comp.ideaChatsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const chatMessage: IChatMessage = { id: 456 };
      const author: IUser = { id: 'f75423bc-28a7-4795-8735-c2cc9fb79678' };
      chatMessage.author = author;
      const globalChat: IGlobalChat = { id: 9958 };
      chatMessage.globalChat = globalChat;
      const ideaChat: IIdeaChat = { id: 31830 };
      chatMessage.ideaChat = ideaChat;

      activatedRoute.data = of({ chatMessage });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(author);
      expect(comp.globalChatsSharedCollection).toContain(globalChat);
      expect(comp.ideaChatsSharedCollection).toContain(ideaChat);
      expect(comp.chatMessage).toEqual(chatMessage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChatMessage>>();
      const chatMessage = { id: 123 };
      jest.spyOn(chatMessageFormService, 'getChatMessage').mockReturnValue(chatMessage);
      jest.spyOn(chatMessageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chatMessage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chatMessage }));
      saveSubject.complete();

      // THEN
      expect(chatMessageFormService.getChatMessage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(chatMessageService.update).toHaveBeenCalledWith(expect.objectContaining(chatMessage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChatMessage>>();
      const chatMessage = { id: 123 };
      jest.spyOn(chatMessageFormService, 'getChatMessage').mockReturnValue({ id: null });
      jest.spyOn(chatMessageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chatMessage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chatMessage }));
      saveSubject.complete();

      // THEN
      expect(chatMessageFormService.getChatMessage).toHaveBeenCalled();
      expect(chatMessageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChatMessage>>();
      const chatMessage = { id: 123 };
      jest.spyOn(chatMessageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chatMessage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(chatMessageService.update).toHaveBeenCalled();
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

    describe('compareGlobalChat', () => {
      it('Should forward to globalChatService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(globalChatService, 'compareGlobalChat');
        comp.compareGlobalChat(entity, entity2);
        expect(globalChatService.compareGlobalChat).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareIdeaChat', () => {
      it('Should forward to ideaChatService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ideaChatService, 'compareIdeaChat');
        comp.compareIdeaChat(entity, entity2);
        expect(ideaChatService.compareIdeaChat).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
