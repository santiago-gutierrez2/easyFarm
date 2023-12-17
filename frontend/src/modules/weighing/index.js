import * as actions from './actions';
import * as actionTypes from './actionTypes';
import reducer from "./reducer";
import * as selectors from './selectors';

export {default as CreateWeighing} from './components/CreateWeighing';
export {default as UpdateWeighing} from './components/UpdateWeighing';
export {default as AllWeighings} from './components/AllWeighings';

// eslint-disable-next-line
export default {actions, actionTypes, reducer, selectors};