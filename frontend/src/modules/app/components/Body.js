import {useSelector} from 'react-redux';
import {Route, Switch} from 'react-router-dom';

import AppGlobalComponents from './AppGlobalComponents';
import Home from './Home';
import {Login, SignUp, UpdateProfile, ChangePassword, Logout, GetEmployees} from '../../users';
import users from '../../users';
import CreateIssue from "../../issues/components/CreateIssue";
import {AllIssues, UpdateIssue} from "../../issues";

const Body = () => {

    const loggedIn = useSelector(users.selectors.isLoggedIn);
    const role = useSelector(users.selectors.getRole);
    
   return (

        <div className="container mw-100">
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
                {!loggedIn && <Route exact path="/users/login"><Login/></Route>}
                <Route><Home/></Route>
            </Switch>
        </div>

    );

};

export default Body;
