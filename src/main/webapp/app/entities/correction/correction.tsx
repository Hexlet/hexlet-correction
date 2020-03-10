import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Row, Table} from 'reactstrap';
import {getSortState, JhiItemCount, JhiPagination, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {IRootState} from 'app/shared/reducers';
import {getEntities} from './correction.reducer';
import {ITEMS_PER_PAGE} from 'app/shared/util/pagination.constants';

export interface ICorrectionProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {
}

export const Correction = (props: ICorrectionProps) => {
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

  const {correctionList, match, loading, totalItems} = props;
  return (
    <div>
      <h2 id="correction-heading">
        <Translate contentKey="hexletCorrectionApp.correction.home.title">Corrections</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="hexletCorrectionApp.correction.home.createLabel">Create new Correction</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {correctionList && correctionList.length > 0 ? (
          <Table responsive>
            <thead>
            <tr>
              <th className="hand" onClick={sort('id')}>
                <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={sort('reporterRemark')}>
                <Translate contentKey="hexletCorrectionApp.correction.reporterRemark">Reporter Remark</Translate>{' '}
                <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={sort('correcterRemark')}>
                <Translate contentKey="hexletCorrectionApp.correction.correcterRemark">Correcter Remark</Translate>{' '}
                <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={sort('resolverRemark')}>
                <Translate contentKey="hexletCorrectionApp.correction.resolverRemark">Resolver Remark</Translate>{' '}
                <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={sort('textBeforeCorrection')}>
                <Translate contentKey="hexletCorrectionApp.correction.textBeforeCorrection">Text Before
                  Correction</Translate>{' '}
                <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={sort('textCorrection')}>
                <Translate contentKey="hexletCorrectionApp.correction.textCorrection">Text Correction</Translate>{' '}
                <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={sort('textAfterCorrection')}>
                <Translate contentKey="hexletCorrectionApp.correction.textAfterCorrection">Text After
                  Correction</Translate>{' '}
                <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={sort('reporterName')}>
                <Translate contentKey="hexletCorrectionApp.correction.reporterName">Reporter Name</Translate>{' '}
                <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={sort('pageURL')}>
                <Translate contentKey="hexletCorrectionApp.correction.pageURL">Page URL</Translate> <FontAwesomeIcon
                icon="sort" />
              </th>
              <th className="hand" onClick={sort('correctionStatus')}>
                <Translate contentKey="hexletCorrectionApp.correction.correctionStatus">Correction
                  Status</Translate>{' '}
                <FontAwesomeIcon icon="sort" />
              </th>
              <th>
                <Translate contentKey="hexletCorrectionApp.correction.correcter">Correcter</Translate> <FontAwesomeIcon
                icon="sort" />
              </th>
              <th>
                <Translate contentKey="hexletCorrectionApp.correction.resolver">Resolver</Translate> <FontAwesomeIcon
                icon="sort" />
              </th>
              <th />
            </tr>
            </thead>
            <tbody>
            {correctionList.map((correction, i) => (
              <tr key={`entity-${i}`}>
                <td>
                  <Button tag={Link} to={`${match.url}/${correction.id}`} color="link" size="sm">
                    {correction.id}
                  </Button>
                </td>
                <td>{correction.reporterRemark}</td>
                <td>{correction.correcterRemark}</td>
                <td>{correction.resolverRemark}</td>
                <td>{correction.textBeforeCorrection}</td>
                <td>{correction.textCorrection}</td>
                <td>{correction.textAfterCorrection}</td>
                <td>{correction.reporterName}</td>
                <td>{correction.pageURL}</td>
                <td>
                  <Translate contentKey={`hexletCorrectionApp.CorrectionStatus.${correction.correctionStatus}`} />
                </td>
                <td>{correction.correcterId ?
                  <Link to={`preference/${correction.correcterId}`}>{correction.correcterId}</Link> : ''}</td>
                <td>{correction.resolverId ?
                  <Link to={`preference/${correction.resolverId}`}>{correction.resolverId}</Link> : ''}</td>
                <td className="text-right">
                  <div className="btn-group flex-btn-group-container">
                    <Button tag={Link} to={`${match.url}/${correction.id}`} color="info" size="sm">
                      <FontAwesomeIcon icon="eye" />{' '}
                      <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                    </Button>
                    <Button
                      tag={Link}
                      to={`${match.url}/${correction.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                      to={`${match.url}/${correction.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="hexletCorrectionApp.correction.home.notFound">No Corrections found</Translate>
            </div>
          )
        )}
      </div>
      <div className={correctionList && correctionList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({correction}: IRootState) => ({
  correctionList: correction.entities,
  loading: correction.loading,
  totalItems: correction.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Correction);
