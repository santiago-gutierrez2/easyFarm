package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.Milking;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;

public interface MilkingService {

    void createMilking(Milking milking) throws InstanceNotFoundException;

    void updateMilking(Long id, Integer liters) throws InstanceNotFoundException;

    void deleteMilkingRecord(Long userId, Long id) throws InstanceNotFoundException, PermissionException;

    Milking getMilkingById(Long id) throws InstanceNotFoundException;

    Block<Milking> getAllMilkings(Long userId, Long animalId, Integer startLiters, Integer endLiters, String startDate, String endDate,
                                  int page, int size) throws InstanceNotFoundException;
}
