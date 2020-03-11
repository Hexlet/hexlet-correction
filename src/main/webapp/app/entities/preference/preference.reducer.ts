import axios from 'axios';
import {ICrudDeleteAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction} from 'react-jhipster';

import {cleanEntity} from 'app/shared/util/entity-utils';
import {FAILURE, REQUEST, SUCCESS} from 'app/shared/reducers/action-type.util';

import {defaultValue, IPreference} from 'app/shared/model/preference.model';

export const ACTION_TYPES = {
  FETCH_PREFERENCE_LIST: 'preference/FETCH_PREFERENCE_LIST',
  FETCH_PREFERENCE: 'preference/FETCH_PREFERENCE',
  CREATE_PREFERENCE: 'preference/CREATE_PREFERENCE',
  UPDATE_PREFERENCE: 'preference/UPDATE_PREFERENCE',
  DELETE_PREFERENCE: 'preference/DELETE_PREFERENCE',
  SET_BLOB: 'preference/SET_BLOB',
  RESET: 'preference/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPreference>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PreferenceState = Readonly<typeof initialState>;

// Reducer

export default (state: PreferenceState = initialState, action): PreferenceState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PREFERENCE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PREFERENCE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PREFERENCE):
    case REQUEST(ACTION_TYPES.UPDATE_PREFERENCE):
    case REQUEST(ACTION_TYPES.DELETE_PREFERENCE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PREFERENCE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PREFERENCE):
    case FAILURE(ACTION_TYPES.CREATE_PREFERENCE):
    case FAILURE(ACTION_TYPES.UPDATE_PREFERENCE):
    case FAILURE(ACTION_TYPES.DELETE_PREFERENCE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PREFERENCE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_PREFERENCE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PREFERENCE):
    case SUCCESS(ACTION_TYPES.UPDATE_PREFERENCE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PREFERENCE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB: {
      const {name, data, contentType} = action.payload;
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

const apiUrl = 'api/preferences';

// Actions

export const getEntities: ICrudGetAllAction<IPreference> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PREFERENCE_LIST,
    payload: axios.get<IPreference>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPreference> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PREFERENCE,
    payload: axios.get<IPreference>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPreference> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PREFERENCE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPreference> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PREFERENCE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPreference> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PREFERENCE,
    payload: axios.delete(requestUrl)
  });
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
