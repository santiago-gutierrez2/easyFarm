import {Link, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getMilkingById, updateMilking} from "../../../backend/milkingService";
import {getAllAnimalsWithLabel} from "../../../backend/animalService";
import {MoonLoader} from "react-spinners";
import {Errors, Success} from "../../common";
import {FormattedMessage} from "react-intl";
import {useDispatch} from "react-redux";
import * as commonActions from "../../app/actions";


const UpdateMilking = () => {

    const {milkingId} = useParams();
    const [editing, setEditing] = useState(false);
    const [milking, setMilking] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [animalSelected, setAnimalSelected] = useState('');
    const [animals, setAnimals] = useState([]);
    const [liters, setLiters] = useState(1);
    const [backendErrors, setBackendErrors] = useState(null);
    const [success, setSuccess] = useState(null);
    let form;
    const dispatch = useDispatch();
    // set active header item.
    dispatch(commonActions.activeItem('Item7'));

    useEffect(() => {
        const id = Number(milkingId);
        if (isLoading) {
            // get milking Info
            getMilkingById(id, (milking) => {
                setMilking(milking);
                setLiters(milking.liters);
                setAnimalSelected(milking.animalMilked);
                // reset backend errors and is loading
                setBackendErrors(null);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            });
            // get Animals
            getAllAnimalsWithLabel(false, true, (animals) => {
                setAnimals(animals);
                // reset backend errors and is loading
                setBackendErrors(null);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            });
        }
    }, [isLoading, milking]);

    function cancelUpdate() {
        setLiters(milking.liters);
        setEditing(false);
    }

    function getAnimaLabel(animalId) {
        for (let animal of animals) {
            if (animal.value === animalId) {
                return animal.label;
            }
        }
    }

    const handleSubmit = event => {
        event.preventDefault();

        if (form.checkValidity()) {
            let milkingDTO = {
                liters: liters,
                animalMilked: Number.parseInt(animalSelected)
            };
            updateMilking(Number(milkingId), milkingDTO,
                () => {
                    var newMilking = {...milking};
                    newMilking.liters = liters;
                    setMilking(newMilking);
                    setSuccess('Milking updated correctly');
                    setEditing(false);
                }, errors => {
                    setBackendErrors(errors);
                }
            );
        } else {
            setBackendErrors(null);
            form.classList.add('was-validated');
        }
    }

    if (isLoading) {
        return (
            <div className="row justify-content-center">
                <div className="col-1 text-center">
                    <MoonLoader color="#97C99D" />
                </div>
            </div>
        );
    }

    return (
      <div className="row justify-content-center fade-in">
          <div className="col-sm-7 col-12">
              <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
              <Success message={success} onClose={() => setSuccess(null)}></Success>
              <div className="card bg-light">
                  <h5 className="card-header card-title-custom">
                      <FormattedMessage id="project.milking.update"/>
                  </h5>
                  <div className="card-body">
                      <form ref={node => form = node}
                            className="needs-validation" noValidate
                            onSubmit={e => handleSubmit(e)}>
                          <div className="form-group row">
                              <label htmlFor="animal" className="col-md-3 col-form-label">
                                  <FormattedMessage id="project.milking.animal"/>
                              </label>
                              <div className="col-md-7">
                                  <p style={{marginTop: '8px'}}>
                                      <Link className="link" to={`/animal/${animalSelected}`}>
                                          {getAnimaLabel(animalSelected)}
                                      </Link>
                                  </p>
                                  <div className="invalid-feedback">
                                      <FormattedMessage id="project.global.validator.required"></FormattedMessage>
                                  </div>
                              </div>
                          </div>
                          <div className="form-group row">
                              <label htmlFor="liters" className="col-md-3 col-form-label">
                                  <FormattedMessage id="project.milking.liters"></FormattedMessage>
                              </label>
                              <div className="col-md-6">
                                  <input type="number" min="1"
                                         value={liters}
                                         required
                                         disabled={!editing}
                                         id="liters"
                                         className="form-control"
                                         onChange={e => setLiters(Number(e.target.value))}/>
                                  <div className="invalid-feedback">
                                      <FormattedMessage id="project.global.validator.required"></FormattedMessage>
                                  </div>
                              </div>
                          </div>
                          <div className="form-group row">
                              {editing &&
                                  <div className="offset-md-3 col-md-3">
                                      <button type="submit" className="btn btn-primary">
                                          <FormattedMessage id="project.global.update"/>
                                      </button>
                                      <button onClick={e => {setEditing(false);cancelUpdate();}}
                                          className="btn btn-danger ml-1">
                                          <FormattedMessage id="project.global.buttons.cancel"/>
                                      </button>
                                  </div>
                              }
                              {!editing &&
                                  <>
                                      <div className="offset-md-3 col-md-2">
                                          <button onClick={e => setEditing(true)}
                                                  className="btn btn-primary">
                                              <FormattedMessage id="project.global.edit"/>
                                          </button>
                                      </div>
                                  </>
                              }
                          </div>
                      </form>
                  </div>
              </div>
          </div>
      </div>
    );
}

export default UpdateMilking;