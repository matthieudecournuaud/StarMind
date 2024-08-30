import { IIdea } from 'app/entities/idea/idea.model';

export interface IAttachment {
  id: number;
  fileName?: string | null;
  fileType?: string | null;
  data?: string | null;
  dataContentType?: string | null;
  relatedIdea?: IIdea | null;
}

export type NewAttachment = Omit<IAttachment, 'id'> & { id: null };
