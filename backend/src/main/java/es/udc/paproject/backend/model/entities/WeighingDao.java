package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface WeighingDao extends PagingAndSortingRepository<Weighing, Long>, CustomizedWeighingDao {

    Slice<Weighing> findByAnimalWeighedBelongsToIdOrderByDateDesc(Long farmId, Pageable pageable);

    List<Weighing> findWeighingByAnimalWeighedId(Long animalId);
}
