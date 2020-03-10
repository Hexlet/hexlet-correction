import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Col, Label, Row} from 'reactstrap';
import {AvForm, AvGroup, AvInput} from 'availity-reactstrap-validation';
import {byteSize, openFile, setFileData, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {IRootState} from 'app/shared/reducers';
import {getUsers} from 'app/modules/administration/user-management/user-management.reducer';
import {createEntity, getEntity, reset, setBlob, updateEntity} from './preference.reducer';

export interface IPreferenceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {
}

export const PreferenceUpdate = (props: IPreferenceUpdateProps) => {
  const [userId, setUserId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const {preferenceEntity, users, loading, updating} = props;

  const {avatar, avatarContentType} = preferenceEntity;

  const handleClose = () => {
    props.history.push('/preference' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...preferenceEntity,
        ...values
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hexletCorrectionApp.preference.home.createOrEditLabel">
            <Translate contentKey="hexletCorrectionApp.preference.home.createOrEditLabel">Create or edit a
              Preference</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : preferenceEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="preference-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="preference-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <AvGroup>
                  <Label id="avatarLabel" for="avatar">
                    <Translate contentKey="hexletCorrectionApp.preference.avatar">Avatar</Translate>
                  </Label>
                  <br />
                  {avatar ? (
                    <div>
                      <a onClick={openFile(avatarContentType, avatar)}>
                        <img src={`data:${avatarContentType};base64,${avatar}`} style={{maxHeight: '100px'}} />
                      </a>
                      <br />
                      <Row>
                        <Col md="11">
                          <span>
                            {avatarContentType}, {byteSize(avatar)}
                          </span>
                        </Col>
                        <Col md="1">
                          <Button color="danger" onClick={clearBlob('avatar')}>
                            <FontAwesomeIcon icon="times-circle" />
                          </Button>
                        </Col>
                      </Row>
                    </div>
                  ) : null}
                  <input id="file_avatar" type="file" onChange={onBlobChange(true, 'avatar')} accept="image/*" />
                  <AvInput type="hidden" name="avatar" value={avatar} />
                </AvGroup>
              </AvGroup>
              <AvGroup>
                <Label for="preference-user">
                  <Translate contentKey="hexletCorrectionApp.preference.user">User</Translate>
                </Label>
                <AvInput id="preference-user" type="select" className="form-control" name="userId">
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/preference" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  preferenceEntity: storeState.preference.entity,
  loading: storeState.preference.loading,
  updating: storeState.preference.updating,
  updateSuccess: storeState.preference.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PreferenceUpdate);
