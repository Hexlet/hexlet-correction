import {Moment} from 'moment';

export interface IComment {
  id?: number;
  message?: string;
  date?: Moment;
  authorId?: number;
  correctionId?: number;
}

export const defaultValue: Readonly<IComment> = {};
