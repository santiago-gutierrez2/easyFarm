import {useDispatch, useSelector} from 'react-redux';
import {Link} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';
import "./HeaderStyle.css";
import * as actions from '../actions';
import users from '../../users';
import app from '../index'


const Header = () => {

    const userName = useSelector(users.selectors.getUserName);
    const userRole = useSelector(users.selectors.getRole);
    const activeItem = useSelector(app.selectors.getActiveItem);
    const dispatch = useDispatch();
    //const [activeItem, setActiveItem] = useState(null);

    const handleItemClick = (item) => {
        dispatch(actions.activeItem(item));
    };

    return (

        <nav className="navbar navbar-expand-lg navbar-dark header-custom">
            <Link className="navbar-brand" to="/" onClick={() => handleItemClick('EasyFarm')}>EasyFarm</Link>
            <button className="navbar-toggler" type="button" 
                data-toggle="collapse" data-target="#navbarSupportedContent" 
                aria-controls="navbarSupportedContent" aria-expanded="false" 
                aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>

            <div className="collapse navbar-collapse" id="navbarSupportedContent">

                <ul className="navbar-nav mr-auto">
                    {/* EMPLOYEES MANAGEMENT */}
                    { userRole === "ADMIN" &&
                        <li className="nav-item dropdown">
                            <a className="dropdown-toggle nav-link" href="/"
                               data-toggle="dropdown" style={{color: activeItem === 'Item1' ? '#fff' : ''}}>
                                <FormattedMessage id="project.employees.title"></FormattedMessage>
                            </a>
                            <div className="dropdown-menu dropdown-menu-right">
                                <Link className="dropdown-item" to="/users/signup">
                                    <FormattedMessage id="project.users.employees.create"/>
                                </Link>
                                <Link className="dropdown-item" to="/users/seeAllEmployees">
                                    <FormattedMessage id="project.users.employees.seeAllEmployees"></FormattedMessage>
                                </Link>
                            </div>
                        </li>
                    }

                    {/* ISSUE MANAGEMENT */}
                    { userRole === "ADMIN" &&
                        <li className="nav-item dropdown">
                            <a className="dropdown-toggle nav-link" href="/"
                               data-toggle="dropdown" style={{color: activeItem === 'Item2' ? '#fff' : ''}}>
                               <FormattedMessage id="project.issues.title" />
                            </a>
                            <div className="dropdown-menu dropdown-menu-right">
                                <Link className="dropdown-item" to="/issues/createIssue">
                                    <FormattedMessage id="project.issues.create"/>
                                </Link>
                                <Link className="dropdown-item" to="/issues/SeeAllIssues">
                                    <FormattedMessage id="project.issues.seeAllIssues" />
                                </Link>
                            </div>
                        </li>
                    }

                    {/* FOODPURCHASE MANAGEMENT */}
                    { userRole === "ADMIN" &&
                        <li  className="nav-item dropdown">
                            <a className="dropdown-toggle nav-link" href="/"
                                data-toggle="dropdown" style={{color: activeItem === 'Item3' ? '#fff' : ''}}>
                                <FormattedMessage id="project.foodPurchase.title"/>
                            </a>
                            <div className="dropdown-menu dropdown-menu-right">
                                <Link className="dropdown-item" to="/foodPurchase/createFoodPurchase">
                                    <FormattedMessage id="project.foodPurchase.create"/>
                                </Link>
                                <Link className="dropdown-item" to="/foodPurchase/allFoodPurchases">
                                    <FormattedMessage id="project.foodPurchase.seeAllFoodPurchases"/>
                                </Link>
                            </div>
                        </li>
                    }

                    {/* ANIMAL MANAGEMENT */}
                    { userRole === "ADMIN" &&
                        <li  className="nav-item dropdown">
                            <a className="dropdown-toggle nav-link" href="/"
                                data-toggle="dropdown" style={{color: activeItem === 'Item4' ? '#fff' : ''}}>
                                <FormattedMessage id="project.animal.title"/>
                            </a>
                            <div className="dropdown-menu dropdown-menu-right">
                                <Link className="dropdown-item" to="/animal/createAnimal">
                                    <FormattedMessage id="project.animal.create"/>
                                </Link>
                                <Link className="dropdown-item" to="/animal/allAnimals">
                                    <FormattedMessage id="project.animal.seeAllAnimals"/>
                                </Link>
                            </div>
                        </li>
                    }
                    { userRole === "EMPLOYEE" &&
                        <li className={activeItem === 'Item4' ? 'nav-item active' : 'nav-item'} >
                            <Link className="nav-link" to="/animal/allAnimals">
                                <FormattedMessage id="project.animal.seeAllAnimals"/>
                            </Link>
                        </li>
                    }

                    {/* FOOD CONSUMPTION MANAGEMENT */}
                    { userName &&
                        <li  className="nav-item dropdown">
                            <a className="dropdown-toggle nav-link" href="/"
                                data-toggle="dropdown" style={{color: activeItem === 'Item5' ? '#fff' : ''}}>
                                <FormattedMessage id="project.foodConsumption.title"/>
                            </a>
                            <div className="dropdown-menu dropdown-menu-right">
                                <Link className="dropdown-item" to="/foodConsumption/CreateFoodConsuption">
                                    <FormattedMessage  id="project.foodConsumption.create"/>
                                </Link>
                                <Link className="dropdown-item" to="/foodConsumption/AllFoodConsumptions">
                                    <FormattedMessage id="project.foodConsumption.list"/>
                                </Link>
                            </div>
                        </li>
                    }

                    {/* WEIGHINGS MANAGEMENT */}
                    { userName &&
                        <li  className="nav-item dropdown">
                            <a className="dropdown-toggle nav-link" href="/"
                                data-toggle="dropdown" style={{color: activeItem === 'Item6' ? '#fff' : ''}}>
                                <FormattedMessage id="project.weighing.title"/>
                            </a>
                            <div className="dropdown-menu dropdown-menu-right">
                                <Link className="dropdown-item" to="/weighing/CreateWeighing">
                                    <FormattedMessage id="project.weighing.create"/>
                                </Link>
                                <Link className="dropdown-item" to="/weighing/AllWeighings">
                                    <FormattedMessage id="project.weighing.List"/>
                                </Link>
                            </div>
                        </li>
                    }

                    {/* MILKING MANAGEMENT */}
                    {userName &&
                        <li className="nav-item dropdown">
                            <a className="dropdown-toggle nav-link" href="/"
                                data-toggle="dropdown" style={{color: activeItem === 'Item7' ? '#fff' : ''}}>
                                <FormattedMessage id="project.milking.title"/>
                            </a>
                            <div className="dropdown-menu dropdown-menu-right">
                                <Link className="dropdown-item" to="/milking/CreateMilking">
                                    <FormattedMessage id="project.milking.create"></FormattedMessage>
                                </Link>
                                <Link className="dropdown-item" to="/milking/AllMilkings">
                                    <FormattedMessage id="project.milking.list"/>
                                </Link>
                            </div>
                        </li>
                    }
                </ul>
                
                {userName ? 

                <ul className="navbar-nav">
                
                    <li className="nav-item dropdown">

                        <a className="dropdown-toggle nav-link" href="/"
                            data-toggle="dropdown">
                            <span className="fas fa-user"></span>&nbsp;
                            {userName}
                        </a>
                        <div className="dropdown-menu dropdown-menu-right">
                            <Link className="dropdown-item" to="/users/update-profile">
                                <FormattedMessage id="project.users.UpdateProfile.title"/>
                            </Link>
                            <Link className="dropdown-item" to="/users/change-password">
                                <FormattedMessage id="project.users.ChangePassword.title"/>
                            </Link>
                            <div className="dropdown-divider"></div>
                            <Link className="dropdown-item" to="/users/logout">
                                <FormattedMessage id="project.app.Header.logout"/>
                            </Link>
                        </div>

                    </li>

                </ul>
                
                :

                <ul className="navbar-nav">
                    <li className="nav-item">
                        <Link className="nav-link" to="/users/login">
                            <FormattedMessage id="project.users.Login.title"/>
                        </Link>
                    </li>
                </ul>
                
                }

            </div>
        </nav>

    );

};

export default Header;
