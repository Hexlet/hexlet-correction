import { Moment } from 'moment';

export interface IComment {
  id?: number;
  message?: string;
  date?: Moment;
  correctionId?: number;
  correcterId?: number;
}

export const defaultValue: Readonly<IComment> = {};
