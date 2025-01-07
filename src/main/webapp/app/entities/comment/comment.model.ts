import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IIdea } from 'app/entities/idea/idea.model';

export interface IComment {
  id: number;
  content?: string | null;
  createdDate?: dayjs.Dayjs | null;
  author?: Pick<IUser, 'id'> | null;
  idea?: Pick<IIdea, 'id'> | null;
}

export type NewComment = Omit<IComment, 'id'> & { id: null };
