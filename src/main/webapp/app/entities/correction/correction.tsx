import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Table } from 'reactstrap';
import { getSortState, JhiItemCount, JhiPagination, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import cn from "classnames";
import { IRootState } from 'app/shared/reducers';
import { getEntities } from './correction.reducer';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

import Card from './card';
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

  const { correctionList, match, loading, totalItems } = props;

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
      <h5>
        Sort:
      <div className="btn-group ml-2" role="group" aria-label="Basic example">
          <button type="button" className="btn btn-info" onClick={sort('reporterName')}>

            <Translate contentKey="hexletCorrectionApp.correction.reporterName">Reporter Name</Translate>{' '}
            <FontAwesomeIcon icon="sort" />
          </button>
          <button type="button" className="btn btn-info" onClick={sort('pageURL')}>
            <Translate contentKey="hexletCorrectionApp.correction.pageURL">Page URL</Translate> <FontAwesomeIcon
              icon="sort" /></button>
          <button type="button" className="btn btn-info" onClick={sort('correctionStatus')} >
            <Translate contentKey="hexletCorrectionApp.correction.correctionStatus">Correction
                  Status</Translate>{' '}
            <FontAwesomeIcon icon="sort" />
          </button>
        </div>
      </h5>
      <div >
        {correctionList && correctionList.length > 0 ? (

          <div >
            {correctionList.map((correction, i) => {
              const status = cn({
                'success': correction.correctionStatus === 'RESOLVED',
                'primary': correction.correctionStatus === 'IN_PROGRESS',
                'info': correction.correctionStatus === 'REPORTED',
                'warning': correction.correctionStatus === 'CANCELED'
              });
              return (

                <div key={`entity-${i}`}
                  className={`card border-${status}`}>
                  <div className={`card-header d-flex justify-content-between`}>
                    <h6 >Page URL : {correction.pageURL}</h6>
                    <h6 >Reporter Name : {correction.reporterName}
                    </h6>
                  </div>
                  <div className={`card-body text-justify row`}>

                    <p>{correction.textBeforeCorrection} <strong className="text-danger">{correction.textCorrection} </strong>{correction.textAfterCorrection} </p>

                  </div>
                  <div className={`card-footer border-${status} text-left text-${status}`}>
                    <p >{correction.correctionStatus}</p>
                  </div>
                  <Button className="mr-2" tag={Link} to={`${match.url}/${correction.id}`} color="info" >
                    <FontAwesomeIcon icon="eye" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.view">View</Translate>
                    </span>
                  </Button>
                </div>

              )
            })}
          </div>
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

const mapStateToProps = ({ correction }: IRootState) => ({
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
