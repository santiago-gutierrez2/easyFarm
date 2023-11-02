import {init} from './appFetch';
import * as userService from './userService';
import * as issueService from './issueService';
import * as foodService from './FoodPurchaseService';

export {default as NetworkError} from "./NetworkError";

// eslint-disable-next-line
export default {init, userService, issueService, foodService};
