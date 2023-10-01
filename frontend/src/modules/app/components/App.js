import {useEffect} from 'react';
import {useDispatch} from 'react-redux';
import {BrowserRouter as Router} from 'react-router-dom';
import '../../../styles.css';

import Header from './Header';
import Body from './Body';
import Footer from './Footer';
import users from '../../users';

const App = () => {

    const dispatch = useDispatch();

    useEffect(() => {

        dispatch(users.actions.tryLoginFromServiceToken(
            () => dispatch(users.actions.logout())));
    
    });

    return (
        <>
        <div>
            <Router>
                <div>
                    <Header/>
                    <Body/>
                </div>
            </Router>
        </div>
            <Footer/>
        </>
    );

}
    
export default App;
