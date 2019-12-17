import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Correcter from './correcter';
import Correction from './correction';
import Comment from './comment';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}correcter`} component={Correcter} />
      <ErrorBoundaryRoute path={`${match.url}correction`} component={Correction} />
      <ErrorBoundaryRoute path={`${match.url}comment`} component={Comment} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
