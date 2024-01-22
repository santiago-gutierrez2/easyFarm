import * as actions from './actions';
import * as actionTypes from './actionTypes';
import reducer from "./reducer";
import * as selectors from './selectors';

export {default as CreateMilking} from './components/CreateMilking';
export {default as UpdateMilking} from './components/UpdateMilking';
export {default as AllMilkings} from './components/AllMilkings';

// eslint-disable-next-line
export default {actions, actionTypes, reducer, selectors};