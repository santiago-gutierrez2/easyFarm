package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Slice;

public interface CustomizedMilkingDao {
    Slice<Milking> findAll(Long farmId, Long animalId, Integer startLiters, Integer endLiters, String startDate, String endDate,
                           int page, int size);
}
