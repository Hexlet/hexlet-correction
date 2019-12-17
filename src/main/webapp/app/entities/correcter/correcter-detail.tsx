import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './correcter.reducer';
import { ICorrecter } from 'app/shared/model/correcter.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICorrecterDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CorrecterDetail extends React.Component<ICorrecterDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { correcterEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="hexletCorrectionApp.correcter.detail.title">Correcter</Translate> [<b>{correcterEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="firstName">
                <Translate contentKey="hexletCorrectionApp.correcter.firstName">First Name</Translate>
              </span>
            </dt>
            <dd>{correcterEntity.firstName}</dd>
            <dt>
              <span id="lastName">
                <Translate contentKey="hexletCorrectionApp.correcter.lastName">Last Name</Translate>
              </span>
            </dt>
            <dd>{correcterEntity.lastName}</dd>
            <dt>
              <span id="status">
                <Translate contentKey="hexletCorrectionApp.correcter.status">Status</Translate>
              </span>
            </dt>
            <dd>{correcterEntity.status}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="hexletCorrectionApp.correcter.email">Email</Translate>
              </span>
            </dt>
            <dd>{correcterEntity.email}</dd>
            <dt>
              <span id="password">
                <Translate contentKey="hexletCorrectionApp.correcter.password">Password</Translate>
              </span>
            </dt>
            <dd>{correcterEntity.password}</dd>
            <dt>
              <span id="phone">
                <Translate contentKey="hexletCorrectionApp.correcter.phone">Phone</Translate>
              </span>
            </dt>
            <dd>{correcterEntity.phone}</dd>
            <dt>
              <span id="avatar">
                <Translate contentKey="hexletCorrectionApp.correcter.avatar">Avatar</Translate>
              </span>
            </dt>
            <dd>
              {correcterEntity.avatar ? (
                <div>
                  <a onClick={openFile(correcterEntity.avatarContentType, correcterEntity.avatar)}>
                    <img src={`data:${correcterEntity.avatarContentType};base64,${correcterEntity.avatar}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {correcterEntity.avatarContentType}, {byteSize(correcterEntity.avatar)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <Translate contentKey="hexletCorrectionApp.correcter.user">User</Translate>
            </dt>
            <dd>{correcterEntity.userLogin ? correcterEntity.userLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/correcter" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/correcter/${correcterEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ correcter }: IRootState) => ({
  correcterEntity: correcter.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CorrecterDetail);
