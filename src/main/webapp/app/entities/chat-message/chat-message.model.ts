import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IGlobalChat } from 'app/entities/global-chat/global-chat.model';
import { IIdeaChat } from 'app/entities/idea-chat/idea-chat.model';

export interface IChatMessage {
  id: number;
  message?: string | null;
  createdDate?: dayjs.Dayjs | null;
  likeCount?: number | null;
  author?: Pick<IUser, 'id'> | null;
  globalChat?: Pick<IGlobalChat, 'id'> | null;
  ideaChat?: Pick<IIdeaChat, 'id'> | null;
}

export type NewChatMessage = Omit<IChatMessage, 'id'> & { id: null };
