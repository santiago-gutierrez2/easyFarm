import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import * as actions from '../actions';
import * as selectors from '../selectors';
import {getAllAnimalsWithLabel} from "../../../backend/animalService";
import {PuffLoader} from "react-spinners";
import {FormattedDate, FormattedMessage} from "react-intl";
import DatePicker from "react-datepicker";
import {Link} from "react-router-dom";
import {Pager} from "../../common";
import * as commonActions from "../../app/actions";

const AllWeighings = () => {

    const [isLoading, setIsLoading] = useState(true);
    const [animalSelected, setAnimalSelected] = useState('0');
    const [startKilos, setStartKilos] = useState(null);
    const [endKilos, setEndKilos] = useState(null);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [animalsOptions, setAnimalsOptions] = useState([]);
    const weighingsSearch = useSelector(selectors.getWeighingsSearch);
    const dispatch = useDispatch();
    const [backendErrors, setBackendErrors] = useState(null);
    // set active header item.
    dispatch(commonActions.activeItem('Item6'));

    useEffect(() => {
        if (isLoading) {
            // get animals
            getAllAnimalsWithLabel(true, false,(animals) => {
                setAnimalsOptions(animals);
                // reset backend errors and is loading
                setBackendErrors(null);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            });
            dispatch(actions.getAllWeighings({page: 0},
                (result) => {
                    console.log(result);
                    setIsLoading(false);
                    setBackendErrors(null);
                }, (errors) => {
                    setBackendErrors(errors);
                    setIsLoading(false);
                }))
        }
    }, [isLoading, dispatch]);

    function handleFilter() {
        let criteria = {
            page: 0,
            animalId: animalSelected,
            startKilos: startKilos,
            endKilos: endKilos,
            startDate: startDate,
            endDate: endDate
        }
        dispatch(actions.getAllWeighings(criteria,
            (result) => {
                console.log(result);
            }, errors => {
                setBackendErrors(errors)
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

    if (!weighingsSearch) {
        return null;
    }

    return (
      <div className="container">
          <div className="row mb-3">
              <div className="col-sm-4">
                  <label className="col-form-label"><FormattedMessage id="project.weighing.animal"/></label>
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
                  <label className="col-form-label"><FormattedMessage id="project.weighing.date"/></label>
                  <DatePicker className="form-control" selected={startDate} onChange={(date) => setStartDate(date)} />
                  <DatePicker className="form-control mt-1" selected={endDate} onChange={(date) => setEndDate(date)} />
              </div>
              <div className="col-sm-3">
                  <label className="col-form-label"><FormattedMessage id="project.weighing.kilosRange"/></label>
                  <input type="number" min="1" value={startKilos} className="form-control"
                         onChange={e => setStartKilos(e.target.value)}/>
                  <input type="number" min="1" value={endKilos} className="form-control mt-1"
                         onChange={e => setEndKilos(e.target.value)}/>
              </div>
              <div className="col-sm-2 text-center">
                  <button className="btn btn-primary" style={{marginTop: '37px'}} onClick={e => handleFilter()}><FormattedMessage id="project.global.search"/></button>
              </div>
          </div>
          {weighingsSearch.result.items.length === 0 &&
              <div className="alert alert-danger" role="alert">
                  <FormattedMessage id="project.weighing.weighingNotFound"/>
              </div>
          }
          {weighingsSearch.result.items.map(weighing => {
              return (
                  <div key={weighing.id} className="card mt-2">
                      <div className="card-header container card-title-custom">
                          <h3>{getAnimalNameAndCode(weighing.animalWeighed)}</h3>
                      </div>
                      <div className="card-body">
                          <p className="card-text"><FormattedMessage id="project.weighing.kilos"/>: {weighing.kilos}</p>
                          <p className="card-text"><FormattedMessage id="project.weighing.date"/>: <FormattedDate value={new Date(weighing.date)}/></p>
                          <Link className="btn btn-primary" to={`/weighing/${weighing.id}`}>
                              <FormattedMessage id="project.weighing.update"/>
                          </Link>
                      </div>
                  </div>
              );
          })}
          <div className="row mt-4">
              <div className="col-12">
                  <Pager
                      back={{
                          enabled: weighingsSearch.criteria.page >= 1,
                          onClick: () => dispatch(actions.previousGetAllWeighings(weighingsSearch.criteria))
                      }}
                      next={{
                          enabled: weighingsSearch.result.existMoreItems,
                          onClick: () => dispatch(actions.nextGetAllWeighings(weighingsSearch.criteria))
                      }}
                  />
              </div>
          </div>
      </div>
    );
}

export default AllWeighings;