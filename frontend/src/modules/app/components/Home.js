import {FormattedMessage} from 'react-intl';
import {Link} from "react-router-dom";
import {useSelector} from "react-redux";
import users from "../../users";

const Home = () => {
    const loggedIn = useSelector(users.selectors.isLoggedIn);

    return (
        !loggedIn &&
        <div className="row justify-content-center">
            <div className="col-9">
                <div className="text-center card w-100">
                    <div className="card-header">
                        <h5 className="card-title"><FormattedMessage id="project.app.Home.welcome"/></h5>
                    </div>
                    <div className="card-body">
                        <p className="card-text"><FormattedMessage id="project.app.Home.login"/></p>
                        <Link to="/users/login">
                            <a className="btn btn-primary">
                                <FormattedMessage id="project.users.Login.title"/>
                            </a>
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
};


export default Home;
