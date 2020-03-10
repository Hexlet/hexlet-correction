import React from 'react';
import {Switch} from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Preference from './preference';
import PreferenceDetail from './preference-detail';
import PreferenceUpdate from './preference-update';
import PreferenceDeleteDialog from './preference-delete-dialog';

const Routes = ({match}) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PreferenceDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PreferenceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PreferenceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PreferenceDetail} />
      <ErrorBoundaryRoute path={match.url} component={Preference} />
    </Switch>
  </>
);

export default Routes;
