import {useSelector} from 'react-redux';
import {Route, Switch} from 'react-router-dom';

import AppGlobalComponents from './AppGlobalComponents';
import Home from './Home';
import {Login, SignUp, UpdateProfile, ChangePassword, Logout, GetEmployees} from '../../users';
import users from '../../users';
import CreateIssue from "../../issues/components/CreateIssue";
import {AllIssues, UpdateIssue} from "../../issues";
import {AllFoodPurchases, CreateFoodPurchase, UpdateFoodPurchase} from "../../foodPurchases";
import {AllAnimals, CreateAnimal, UpdateAnimal} from "../../animals";
import CreateFoodConsumtion from "../../foodConsumptions/components/CreateFoodConsumtion";
import {AllFoodConsumptions} from "../../foodConsumptions";
import {AllWeighings, CreateWeighing, UpdateWeighing} from "../../weighing";
import {AllMilkings, CreateMilking} from "../../Milking";
import UpdateMilking from "../../Milking/components/UpdateMilking";

const Body = () => {

    const loggedIn = useSelector(users.selectors.isLoggedIn);
    const role = useSelector(users.selectors.getRole);
    
   return (

        <div className="container mw-100 mb-5">
            <br/>
            <AppGlobalComponents/>
            <Switch>
                <Route exact path="/"><Home/></Route>
                {loggedIn && <Route exact path="/users/update-profile"><UpdateProfile/></Route>}
                {loggedIn && <Route exact path="/users/change-password"><ChangePassword/></Route>}
                {loggedIn && <Route exact path="/users/logout"><Logout/></Route>}
                {loggedIn && role == 'ADMIN' && <Route exact path="/users/signup"><SignUp/></Route>}
                {loggedIn && role == 'ADMIN' && <Route exact path="/users/seeAllEmployees"><GetEmployees/></Route>}
                {loggedIn && role == 'ADMIN' && <Route exact path="/issues/createIssue"><CreateIssue/></Route>}
                {loggedIn && role == 'ADMIN' && <Route exact path="/issues/SeeAllIssues"> <AllIssues/> </Route>}
                {loggedIn && role == 'ADMIN' && <Route exact path="/issues/:issueId"> <UpdateIssue/></Route>}
                {loggedIn && role == 'ADMIN' && <Route exact path="/foodPurchase/createFoodPurchase"> <CreateFoodPurchase/> </Route>}
                {loggedIn && role == 'ADMIN' && <Route exact path="/foodPurchase/allFoodPurchases"> <AllFoodPurchases/> </Route> }
                {loggedIn && role == 'ADMIN' && <Route exact path="/foodPurchase/:foodPurchaseId"> <UpdateFoodPurchase/></Route>}
                {loggedIn && role == 'ADMIN' && <Route exact path="/animal/createAnimal"> <CreateAnimal/></Route>}
                {loggedIn && <Route exact path="/animal/allAnimals"> <AllAnimals/> </Route>}
                {loggedIn && <Route exact path="/animal/:animalId"> <UpdateAnimal/> </Route>}
                {loggedIn && <Route exact path="/foodConsumption/CreateFoodConsuption"> <CreateFoodConsumtion/> </Route>}
                {loggedIn && <Route exact path="/foodConsumption/AllFoodConsumptions"> <AllFoodConsumptions/> </Route>}
                {loggedIn && <Route exact path="/weighing/CreateWeighing"> <CreateWeighing/> </Route>}
                {loggedIn && <Route exact path="/weighing/AllWeighings"> <AllWeighings/> </Route>}
                {loggedIn && <Route exact path="/weighing/:weighingId"> <UpdateWeighing/> </Route>}
                {loggedIn && <Route exact path="/milking/CreateMilking"> <CreateMilking/> </Route>}
                {loggedIn && <Route exact path="/milking/AllMilkings"> <AllMilkings/> </Route>}
                {loggedIn && <Route exact path="/milking/:milkingId"> <UpdateMilking/> </Route>}
                {!loggedIn && <Route exact path="/users/login"><Login/></Route>}
                <Route><Home/></Route>
            </Switch>
        </div>

    );

};

export default Body;
