import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './correcter.reducer';
import { ICorrecter } from 'app/shared/model/correcter.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICorrecterUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICorrecterUpdateState {
  isNew: boolean;
  userId: string;
}

export class CorrecterUpdate extends React.Component<ICorrecterUpdateProps, ICorrecterUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { correcterEntity } = this.props;
      const entity = {
        ...correcterEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/correcter');
  };

  render() {
    const { correcterEntity, users, loading, updating } = this.props;
    const { isNew } = this.state;

    const { avatar, avatarContentType } = correcterEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="hexletCorrectionApp.correcter.home.createOrEditLabel">
              <Translate contentKey="hexletCorrectionApp.correcter.home.createOrEditLabel">Create or edit a Correcter</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : correcterEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="correcter-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="correcter-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="firstNameLabel" for="correcter-firstName">
                    <Translate contentKey="hexletCorrectionApp.correcter.firstName">First Name</Translate>
                  </Label>
                  <AvField
                    id="correcter-firstName"
                    type="text"
                    name="firstName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      minLength: { value: 1, errorMessage: translate('entity.validation.minlength', { min: 1 }) },
                      maxLength: { value: 50, errorMessage: translate('entity.validation.maxlength', { max: 50 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastNameLabel" for="correcter-lastName">
                    <Translate contentKey="hexletCorrectionApp.correcter.lastName">Last Name</Translate>
                  </Label>
                  <AvField
                    id="correcter-lastName"
                    type="text"
                    name="lastName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      minLength: { value: 1, errorMessage: translate('entity.validation.minlength', { min: 1 }) },
                      maxLength: { value: 50, errorMessage: translate('entity.validation.maxlength', { max: 50 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel" for="correcter-status">
                    <Translate contentKey="hexletCorrectionApp.correcter.status">Status</Translate>
                  </Label>
                  <AvInput
                    id="correcter-status"
                    type="select"
                    className="form-control"
                    name="status"
                    value={(!isNew && correcterEntity.status) || 'NON_ACTIVATED'}
                  >
                    <option value="NON_ACTIVATED">{translate('hexletCorrectionApp.CorrecterStatus.NON_ACTIVATED')}</option>
                    <option value="BLOCKED">{translate('hexletCorrectionApp.CorrecterStatus.BLOCKED')}</option>
                    <option value="READY">{translate('hexletCorrectionApp.CorrecterStatus.READY')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="correcter-email">
                    <Translate contentKey="hexletCorrectionApp.correcter.email">Email</Translate>
                  </Label>
                  <AvField
                    id="correcter-email"
                    type="text"
                    name="email"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="passwordLabel" for="correcter-password">
                    <Translate contentKey="hexletCorrectionApp.correcter.password">Password</Translate>
                  </Label>
                  <AvField
                    id="correcter-password"
                    type="text"
                    name="password"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      minLength: { value: 8, errorMessage: translate('entity.validation.minlength', { min: 8 }) },
                      maxLength: { value: 50, errorMessage: translate('entity.validation.maxlength', { max: 50 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="phoneLabel" for="correcter-phone">
                    <Translate contentKey="hexletCorrectionApp.correcter.phone">Phone</Translate>
                  </Label>
                  <AvField
                    id="correcter-phone"
                    type="text"
                    name="phone"
                    validate={{
                      minLength: { value: 8, errorMessage: translate('entity.validation.minlength', { min: 8 }) },
                      maxLength: { value: 50, errorMessage: translate('entity.validation.maxlength', { max: 50 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="avatarLabel" for="avatar">
                      <Translate contentKey="hexletCorrectionApp.correcter.avatar">Avatar</Translate>
                    </Label>
                    <br />
                    {avatar ? (
                      <div>
                        <a onClick={openFile(avatarContentType, avatar)}>
                          <img src={`data:${avatarContentType};base64,${avatar}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {avatarContentType}, {byteSize(avatar)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('avatar')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_avatar" type="file" onChange={this.onBlobChange(true, 'avatar')} accept="image/*" />
                    <AvInput type="hidden" name="avatar" value={avatar} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label for="correcter-user">
                    <Translate contentKey="hexletCorrectionApp.correcter.user">User</Translate>
                  </Label>
                  <AvInput id="correcter-user" type="select" className="form-control" name="userId" required>
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                  <AvFeedback>
                    <Translate contentKey="entity.validation.required">This field is required.</Translate>
                  </AvFeedback>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/correcter" replace color="info">
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
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  correcterEntity: storeState.correcter.entity,
  loading: storeState.correcter.loading,
  updating: storeState.correcter.updating,
  updateSuccess: storeState.correcter.updateSuccess
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

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CorrecterUpdate);
