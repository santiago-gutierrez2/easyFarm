import {combineReducers} from 'redux';

import app from '../modules/app';
import users from '../modules/users';
import issues from '../modules/issues';
import foodPurchases from "../modules/foodPurchases";
import animals from "../modules/animals";

const rootReducer = combineReducers({
    app: app.reducer,
    users: users.reducer,
    issues: issues.reducer,
    foodPurchases: foodPurchases.reducer,
    animals: animals.reducer
});

export default rootReducer;
