package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.Weighing;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.rest.dtos.WeighingDTOs.WeighingDto;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface WeighingService {

    void createWeighing(Weighing weighing) throws InstanceNotFoundException;

    void updateWeighing(Long id, Integer kilos) throws InstanceNotFoundException;

    void deleteWeighing(Long userId, Long id) throws InstanceNotFoundException, PermissionException;

    Weighing getWeighingById(Long id) throws InstanceNotFoundException;

    Block<Weighing> getAllWeighings(Long userId, Long animalId, Integer startKilos, Integer endKilos, String startDate, String endDate,
            int page, int size) throws InstanceNotFoundException;

    List<WeighingDto> getAllWeighingByAnimalId(Long userId, Long animalId) throws InstanceNotFoundException;
}
