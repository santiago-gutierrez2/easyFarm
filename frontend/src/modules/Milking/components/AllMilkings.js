import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import * as selectors from '../selectors';
import {getAllAnimalsWithLabel} from "../../../backend/animalService";
import * as actions from '../actions';
import {PuffLoader} from "react-spinners";
import {FormattedDate, FormattedMessage} from "react-intl";
import DatePicker from "react-datepicker";
import milking from "../index";
import {Link} from "react-router-dom";
import {Pager} from "../../common";
import * as commonActions from "../../app/actions";

const AllMilkings = () => {

    const [isLoading, setIsLoading] = useState(true);
    const [animalSelected, setAnimalSelected] = useState('0');
    const [startLiters, setStartLiters] = useState(null);
    const [endLiters, setEndLiters] = useState(null);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [animalsOptions, setAnimalsOptions] = useState([]);
    const milkingsSearch = useSelector(selectors.getMilkingSearch);
    const dispatch = useDispatch();
    const [backendErrors, setBackendErrors] = useState(null);
    // set active header item.
    dispatch(commonActions.activeItem('Item7'));

    useEffect(() => {
        if (isLoading) {
            // get animals
            getAllAnimalsWithLabel(true, true, (animals) => {
                setAnimalsOptions(animals);
                // reset backend errors and isLoading
                setBackendErrors(null);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            });
            dispatch(actions.getAllMilkings({page: 0},
                (result) => {
                    console.log(result);
                    setIsLoading(false);
                    setBackendErrors(null);
                }, errors => {
                    setBackendErrors(errors);
                    setIsLoading(false);
                }))
        }
    }, [isLoading, dispatch]);

    function handleFilter() {
        let criteria = {
            page: 0,
            animalId: animalSelected,
            startLiters: startLiters,
            endLiters: endLiters,
            startDate: startDate,
            endDate: endDate
        }
        dispatch(actions.getAllMilkings(criteria,
            (result) => {
                console.log(result);
            }, errors => {
                setBackendErrors(errors);
            })
        );
    }

    function getAnimalNameAndCode(animalId) {
        for (let animal of animalsOptions) {
            if (animal.value === animalId) {
                return animal.label;
            }
        }
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

    if (!milkingsSearch) {
        return null;
    }

    return (
      <div className="container">
          <div className="row mb-3">
              <div className="col-sm-4">
                  <label className="col-form-label"><FormattedMessage id="project.milking.animal"/></label>
                  <select className="form-control" id="animal"
                        value={animalSelected}
                        onChange={e => setAnimalSelected(e.target.value)}>
                      <option key={0} value="0">-- All options --</option>
                      {animalsOptions && animalsOptions.map(a =>
                      <option key={a.value} value={a.value}>{a.label}</option>
                      )}
                  </select>
              </div>
              <div className="col-sm-3">
                  <label className="col-form-label"><FormattedMessage id="project.milking.date"/></label>
                  <DatePicker className="form-control" selected={startDate} onChange={(date) => setStartDate(date)} />
                  <DatePicker className="form-control mt-1" selected={endDate} onChange={(date) => setEndDate(date)} />
              </div>
              <div className="col-sm-3">
                  <label className="col-form-label"><FormattedMessage id="project.milking.litersRange"/></label>
                  <input type="number" min="1" value={startLiters} className="form-control"
                         onChange={e => setStartLiters(e.target.value)}/>
                  <input type="number" min="1" value={endLiters} className="form-control mt-1"
                         onChange={e => setEndLiters(e.target.value)}/>
              </div>
              <div className="col-sm-2 text-center">
                  <button className="btn btn-primary" style={{marginTop: '37px'}} onClick={e => handleFilter()}><FormattedMessage id="project.global.search"/></button>
              </div>
          </div>
          {milkingsSearch.result.items.length === 0 &&
                <div className="alert alert-danger" role="alert">
                    <FormattedMessage id="project.milking.milkingNotFound"/>
                </div>
          }
          {milkingsSearch.result.items.map(milk => {
              return (
                <div key={milk.id} className="card mt-2">
                    <div className="card-header container card-title-custom">
                        <h3>{getAnimalNameAndCode(milk.animalMilked)}</h3>
                    </div>
                    <div className="card-body">
                        <p className="card-text"><FormattedMessage id="project.milking.liters"/>: {milk.liters}</p>
                        <p className="card-text"><FormattedMessage id="project.milking.date"/>: <FormattedDate value={new Date(milk.date)}/></p>
                        <Link className="btn btn-primary" to={`/milking/${milk.id}`}>
                            <FormattedMessage id="project.milking.update"/>
                        </Link>
                    </div>
                </div>
              );
          })}
          <div className="row mt-4">
              <div className="col-12">
                  <Pager
                      back={{
                          enabled: milkingsSearch.criteria.page >= 1,
                          onClick: () => dispatch(actions.previousGetAllMilkings(milkingsSearch.criteria))
                      }}
                      next={{
                          enabled: milkingsSearch.result.existMoreItems,
                          onClick: () => dispatch(actions.nextGetAllMilkings(milkingsSearch.criteria))
                      }}
                  />
              </div>
          </div>
      </div>
    );
}

export default AllMilkings;