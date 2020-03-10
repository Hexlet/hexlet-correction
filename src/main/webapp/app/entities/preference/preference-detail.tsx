import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {byteSize, openFile, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {IRootState} from 'app/shared/reducers';
import {getEntity} from './preference.reducer';

export interface IPreferenceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {
}

export const PreferenceDetail = (props: IPreferenceDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const {preferenceEntity} = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate
            contentKey="hexletCorrectionApp.preference.detail.title">Preference</Translate> [<b>{preferenceEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="avatar">
              <Translate contentKey="hexletCorrectionApp.preference.avatar">Avatar</Translate>
            </span>
          </dt>
          <dd>
            {preferenceEntity.avatar ? (
              <div>
                <a onClick={openFile(preferenceEntity.avatarContentType, preferenceEntity.avatar)}>
                  <img src={`data:${preferenceEntity.avatarContentType};base64,${preferenceEntity.avatar}`}
                       style={{maxHeight: '30px'}} />
                </a>
                <span>
                  {preferenceEntity.avatarContentType}, {byteSize(preferenceEntity.avatar)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="hexletCorrectionApp.preference.user">User</Translate>
          </dt>
          <dd>{preferenceEntity.userId ? preferenceEntity.userId : ''}</dd>
        </dl>
        <Button tag={Link} to="/preference" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/preference/${preferenceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({preference}: IRootState) => ({
  preferenceEntity: preference.entity
});

const mapDispatchToProps = {getEntity};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PreferenceDetail);
