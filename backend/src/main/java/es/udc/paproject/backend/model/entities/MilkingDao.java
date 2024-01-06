package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MilkingDao extends PagingAndSortingRepository<Milking, Long>, CustomizedMilkingDao {

    Slice<Milking> findByAnimalMilkedBelongsToIdOrderByDateDesc(Long farmId, Pageable pageable);
}
