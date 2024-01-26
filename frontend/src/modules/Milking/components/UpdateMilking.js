import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getMilkingById, updateMilking} from "../../../backend/milkingService";
import {getAllAnimalsWithLabel} from "../../../backend/animalService";
import {MoonLoader} from "react-spinners";
import {Errors, Success} from "../../common";
import {FormattedMessage} from "react-intl";


const UpdateMilking = () => {

    const {milkingId} = useParams();
    const [milking, setMilking] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [animalSelected, setAnimalSelected] = useState('');
    const [animals, setAnimals] = useState([]);
    const [liters, setLiters] = useState(1);
    const [backendErrors, setBackendErrors] = useState(null);
    const [success, setSuccess] = useState(null);
    let form;

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

    const handleSubmit = event => {
        event.preventDefault();

        if (form.checkValidity()) {
            let milkingDTO = {
                liters: liters,
                animalMilked: Number.parseInt(animalSelected)
            };
            updateMilking(Number(milkingId), milkingDTO,
                () => {
                    setSuccess('Milking updated correctly');
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
                              <div className="col-md-6">
                                  <select id="animal" className="form-control" required
                                          value={animalSelected}
                                          disabled>
                                      {animals && animals.map(a =>
                                          <option key={a.value} value={a.value}>{a.label}</option>
                                      )}
                                  </select>
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
                                         id="liters"
                                         className="form-control"
                                         onChange={e => setLiters(Number(e.target.value))}/>
                                  <div className="invalid-feedback">
                                      <FormattedMessage id="project.global.validator.required"></FormattedMessage>
                                  </div>
                              </div>
                          </div>
                          <div className="form-group row">
                              <div className="offset-md-3 col-md-2">
                                  <button type="submit" className="btn btn-primary">
                                      <FormattedMessage id="project.global.update"/>
                                  </button>
                              </div>
                          </div>
                      </form>
                  </div>
              </div>
          </div>
      </div>
    );
}

export default UpdateMilking;