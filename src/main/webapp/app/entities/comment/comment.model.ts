import dayjs from 'dayjs/esm';
import { IIdea } from 'app/entities/idea/idea.model';
import { IUser } from 'app/entities/user/user.model';

export interface IComment {
  id: number;
  content?: string | null;
  createdDate?: dayjs.Dayjs | null;
  relatedIdea?: IIdea | null;
  author?: Pick<IUser, 'id'> | null;
  idea?: IIdea | null;
}

export type NewComment = Omit<IComment, 'id'> & { id: null };
