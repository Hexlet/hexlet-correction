import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Row, Table} from 'reactstrap';
import {byteSize, getSortState, JhiItemCount, JhiPagination, openFile, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {IRootState} from 'app/shared/reducers';
import {getEntities} from './preference.reducer';
import {ITEMS_PER_PAGE} from 'app/shared/util/pagination.constants';

export interface IPreferenceProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {
}

export const Preference = (props: IPreferenceProps) => {
  const [paginationState, setPaginationState] = useState(getSortState(props.location, ITEMS_PER_PAGE));

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
  };

  const sortEntities = () => {
    getAllEntities();
    props.history.push(
      `${props.location.pathname}?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`
    );
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage
    });

  const {preferenceList, match, loading, totalItems} = props;
  return (
    <div>
      <h2 id="preference-heading">
        <Translate contentKey="hexletCorrectionApp.preference.home.title">Preferences</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="hexletCorrectionApp.preference.home.createLabel">Create new Preference</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {preferenceList && preferenceList.length > 0 ? (
          <Table responsive>
            <thead>
            <tr>
              <th className="hand" onClick={sort('id')}>
                <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={sort('avatar')}>
                <Translate contentKey="hexletCorrectionApp.preference.avatar">Avatar</Translate> <FontAwesomeIcon
                icon="sort" />
              </th>
              <th>
                <Translate contentKey="hexletCorrectionApp.preference.user">User</Translate> <FontAwesomeIcon
                icon="sort" />
              </th>
              <th />
            </tr>
            </thead>
            <tbody>
            {preferenceList.map((preference, i) => (
              <tr key={`entity-${i}`}>
                <td>
                  <Button tag={Link} to={`${match.url}/${preference.id}`} color="link" size="sm">
                    {preference.id}
                  </Button>
                </td>
                <td>
                  {preference.avatar ? (
                    <div>
                      <a onClick={openFile(preference.avatarContentType, preference.avatar)}>
                        <img src={`data:${preference.avatarContentType};base64,${preference.avatar}`}
                             style={{maxHeight: '30px'}} />
                        &nbsp;
                      </a>
                      <span>
                          {preference.avatarContentType}, {byteSize(preference.avatar)}
                        </span>
                    </div>
                  ) : null}
                </td>
                <td>{preference.userId ? preference.userId : ''}</td>
                <td className="text-right">
                  <div className="btn-group flex-btn-group-container">
                    <Button tag={Link} to={`${match.url}/${preference.id}`} color="info" size="sm">
                      <FontAwesomeIcon icon="eye" />{' '}
                      <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                    </Button>
                    <Button
                      tag={Link}
                      to={`${match.url}/${preference.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                      color="primary"
                      size="sm"
                    >
                      <FontAwesomeIcon icon="pencil-alt" />{' '}
                      <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                    </Button>
                    <Button
                      tag={Link}
                      to={`${match.url}/${preference.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                      color="danger"
                      size="sm"
                    >
                      <FontAwesomeIcon icon="trash" />{' '}
                      <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                    </Button>
                  </div>
                </td>
              </tr>
            ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="hexletCorrectionApp.preference.home.notFound">No Preferences found</Translate>
            </div>
          )
        )}
      </div>
      <div className={preferenceList && preferenceList.length > 0 ? '' : 'd-none'}>
        <Row className="justify-content-center">
          <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage}
                        i18nEnabled />
        </Row>
        <Row className="justify-content-center">
          <JhiPagination
            activePage={paginationState.activePage}
            onSelect={handlePagination}
            maxButtons={5}
            itemsPerPage={paginationState.itemsPerPage}
            totalItems={props.totalItems}
          />
        </Row>
      </div>
    </div>
  );
};

const mapStateToProps = ({preference}: IRootState) => ({
  preferenceList: preference.entities,
  loading: preference.loading,
  totalItems: preference.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Preference);
