import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import * as selectors from "../selectors";
import * as actions from "../actions";
import {MoonLoader, PuffLoader} from "react-spinners";
import {FormattedDate, FormattedMessage} from "react-intl";
import DatePicker from "react-datepicker";
import {Link} from "react-router-dom";
import {Pager} from "../../common";
import users from "../../users";
import * as commonActions from "../../app/actions";
import Breadcrumb from "react-bootstrap/Breadcrumb";


const AllAnimals = () => {

    const role = useSelector(users.selectors.getRole);
    const [isLoading, setIsLoading] = useState(true);
    const [name, setName] = useState('');
    const [identificationNumber, setIdentificationNumber] = useState('');
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [isMale, setIsMale] = useState('');
    const [dead, setDead] = useState('false');
    const animalsSearch = useSelector(selectors.getAnimalsSearch);
    const dispatch = useDispatch();
    const [backendErrors, setBackendErrors] = useState(null);
    // set active header item.
    dispatch(commonActions.activeItem('Item4'));

    useEffect(() => {
        if (isLoading) {
            dispatch(actions.getAllAnimals({page: 0, dead: dead},
                (result) => {
                    console.log(result);
                    setIsLoading(false);
                    setBackendErrors(null);
                }, (errors) => {
                    setBackendErrors(errors);
                    setIsLoading(false);
                })
            );
        }
    }, [isLoading, dispatch]);

    function handleFilter() {
        let criteria = {
            page: 0,
            name: name.length > 0 ? name : null,
            identificationNumber: identificationNumber,
            startDate: startDate != null ? startDate : null,
            endDate: endDate != null ? endDate : null,
            isMale: isMale === "" ? null : isMale,
            dead: dead === "" ? null : dead
        }
        dispatch(actions.getAllAnimals(criteria,
            (result) => {
                console.log(result);
            }, errors => {
                setBackendErrors(errors);
            }))
    }


    if (isLoading) {
        return (
            <div className="row justify-content-center">
                <div className="col-1 text-center">
                    <PuffLoader color="#97C99D" />
                </div>
            </div>
        );
    }

    if (!animalsSearch) {
        return null;
    }

    return (
        <>
            <Breadcrumb>
                <Breadcrumb.Item href="/">Dashboard</Breadcrumb.Item>
                <Breadcrumb.Item active>Livestock list</Breadcrumb.Item>
            </Breadcrumb>
            <div className="container">
                <div className="row mb-3">
                    <div className="col-sm-3">
                        <label className="col-form-label"><FormattedMessage id="project.animal.name"/>: </label>
                        <input type="text" placeholder="animal name" className="form-control" value={name}
                               onChange={e => setName(e.target.value)}/>
                    </div>
                    <div className="col-sm-2">
                        <label className="col-form-label"><FormattedMessage id="project.animal.identificationNumber"/>:
                        </label>
                        <input type="text" className="form-control" value={identificationNumber}
                               onChange={e => setIdentificationNumber(e.target.value)}/>
                    </div>
                    <div className="col-sm-2">
                        <label className="col-form-label"><FormattedMessage id="project.issue.state"/>: </label>
                        <select className="form-control" value={dead} onChange={e => setDead(e.target.value)}>
                            <option key={0} value="">{/*<FormattedMessage id="project.global.all"/>*/}All</option>
                            <option key={1} value="false">{/*<FormattedMessage id="project.issue.active"/>*/}Available
                            </option>
                            <option key={2} value="true">{/*<FormattedMessage id="project.issue.done" />*/}Unavailable</option>
                        </select>
                    </div>
                    <div className="col-sm-3">
                        <label className="col-form-label"><FormattedMessage id="project.issues.creationDate"/>: </label>
                        <div className="w-100"></div>
                        <DatePicker className="form-control" selected={startDate}
                                    onChange={(date) => setStartDate(date)}/>
                        <DatePicker className="form-control mt-1" selected={endDate}
                                    onChange={(date) => setEndDate(date)}/>
                    </div>
                    <div className="col-sm-2 text-center">
                        <button className="btn btn-primary" style={{marginTop: '37px'}} onClick={e => handleFilter()}>
                            <FormattedMessage id="project.global.search"/></button>
                    </div>
                </div>
                {animalsSearch.result.items.length === 0 &&
                    <div className="alert alert-danger" role="alert">
                        <FormattedMessage id='project.animal.animalsNotFound'/>
                    </div>
                }
                {animalsSearch.result.items.map(animal => {
                    return (
                        <div key={animal.id} className="card mt-2 bg-light">
                            <div className="container card-header card-title-custom">
                                <div className="row">
                                    <div className="col-xl-10 col-7">
                                        <h3>{animal.identificationNumber}</h3>
                                    </div>
                                    <div className="col-xl-2 col-5 align-self-center">
                                        <h3>
                                            {animal.dead &&
                                                <span className="badge badge-danger float-right">
                                                        Unavailable
                                                    </span>
                                            }
                                        </h3>
                                    </div>
                                </div>
                            </div>
                            <div className="card-body">
                                <p className="card-text">{animal.physicalDescription}</p>
                                <p className="card-text"><FormattedMessage id="project.animal.name"/>: {animal.name}</p>
                                <p className="card-text"><FormattedMessage
                                    id="project.animal.birthDate"/>: <FormattedDate value={new Date(animal.birthDate)}/>
                                </p>
                                <p className="card-text"><FormattedMessage id="project.animal.gender"/>: {animal.male ?
                                    <FormattedMessage id="project.animal.male"/> :
                                    <FormattedMessage id="project.animal.female"/>}</p>
                                {
                                    <Link className="btn btn-primary" to={`/animal/${animal.id}`}>
                                        <FormattedMessage id="project.global.seeDetails"/>
                                    </Link>
                                }
                            </div>
                        </div>
                    )
                })}
                <div className="row mt-4">
                    <div className="col-12">
                        <Pager
                            back={{
                                enabled: animalsSearch.criteria.page >= 1,
                                onClick: () => dispatch(actions.previousGetAllAnimals(animalsSearch.criteria))
                            }}
                            next={{
                                enabled: animalsSearch.result.existMoreItems,
                                onClick: () => dispatch(actions.nextGetAllAnimals(animalsSearch.criteria))
                            }}
                        />
                    </div>
                </div>
            </div>
        </>
    );
}

export default AllAnimals;