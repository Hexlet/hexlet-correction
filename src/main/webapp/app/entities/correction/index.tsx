import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Correction from './correction';
import CorrectionDetail from './correction-detail';
import CorrectionUpdate from './correction-update';
import CorrectionDeleteDialog from './correction-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CorrectionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CorrectionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CorrectionDetail} />
      <ErrorBoundaryRoute path={match.url} component={Correction} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CorrectionDeleteDialog} />
  </>
);

export default Routes;
