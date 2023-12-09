package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Slice;

public interface CustomizedWeighingDao {
    Slice<Weighing> find(Long farmId, Long animalId, Integer startKilos, Integer endKilos, String startDate, String endDate,
                         int page, int size);
}
