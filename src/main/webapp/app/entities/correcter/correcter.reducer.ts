import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICorrecter, defaultValue } from 'app/shared/model/correcter.model';

export const ACTION_TYPES = {
  FETCH_CORRECTER_LIST: 'correcter/FETCH_CORRECTER_LIST',
  FETCH_CORRECTER: 'correcter/FETCH_CORRECTER',
  CREATE_CORRECTER: 'correcter/CREATE_CORRECTER',
  UPDATE_CORRECTER: 'correcter/UPDATE_CORRECTER',
  DELETE_CORRECTER: 'correcter/DELETE_CORRECTER',
  SET_BLOB: 'correcter/SET_BLOB',
  RESET: 'correcter/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICorrecter>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CorrecterState = Readonly<typeof initialState>;

// Reducer

export default (state: CorrecterState = initialState, action): CorrecterState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CORRECTER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CORRECTER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CORRECTER):
    case REQUEST(ACTION_TYPES.UPDATE_CORRECTER):
    case REQUEST(ACTION_TYPES.DELETE_CORRECTER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CORRECTER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CORRECTER):
    case FAILURE(ACTION_TYPES.CREATE_CORRECTER):
    case FAILURE(ACTION_TYPES.UPDATE_CORRECTER):
    case FAILURE(ACTION_TYPES.DELETE_CORRECTER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CORRECTER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_CORRECTER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CORRECTER):
    case SUCCESS(ACTION_TYPES.UPDATE_CORRECTER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CORRECTER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/correcters';

// Actions

export const getEntities: ICrudGetAllAction<ICorrecter> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CORRECTER_LIST,
    payload: axios.get<ICorrecter>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICorrecter> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CORRECTER,
    payload: axios.get<ICorrecter>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICorrecter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CORRECTER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICorrecter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CORRECTER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICorrecter> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CORRECTER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
