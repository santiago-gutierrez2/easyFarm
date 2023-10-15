import {combineReducers} from 'redux';

import app from '../modules/app';
import users from '../modules/users';
import issues from '../modules/issues'

const rootReducer = combineReducers({
    app: app.reducer,
    users: users.reducer,
    issues: issues.reducer
});

export default rootReducer;
