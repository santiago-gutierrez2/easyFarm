import {init} from './appFetch';
import * as userService from './userService';
import * as issueService from './issueService';
import * as foodService from './FoodPurchaseService';
import * as animalService from './animalService';
import * as foodConsumptionService from './foodConsumptionService';
import * as weighingService from './weighingService';
import * as milkingService from  './milkingService';

export {default as NetworkError} from "./NetworkError";

// eslint-disable-next-line
export default {init, userService, issueService, foodService, animalService, foodConsumptionService, weighingService, milkingService};
