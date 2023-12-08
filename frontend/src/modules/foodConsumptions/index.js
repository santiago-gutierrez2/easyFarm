import * as actions from './actions';
import * as actionTypes from './actionTypes';
import reducer from "./reducer";
import * as selectors from './selectors';

export {default as CreateFoodConsumption} from './components/CreateFoodConsumtion';
export {default as AllFoodConsumptions} from './components/AllFoodConsumptions';

// eslint-disable-next-line
export default {actions, actionTypes, reducer, selectors};