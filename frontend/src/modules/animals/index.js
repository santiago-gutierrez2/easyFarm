import * as actions from './actions';
import * as actionTypes from './actionTypes';
import reducer from "./reducer";
import * as selectors from './selectors';

export {default as CreateAnimal} from './components/CreateAnimal';
export {default as UpdateAnimal} from './components/UpdateAnimal';
export {default as AllAnimals} from './components/AllAnimals';

// eslint-disable-next-line
export default {actions, actionTypes, reducer, selectors};