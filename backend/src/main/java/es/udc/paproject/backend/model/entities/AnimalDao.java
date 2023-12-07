package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AnimalDao extends PagingAndSortingRepository<Animal, Long>, CustomizedAnimalDao {
    Slice<Animal> findByBelongsToIdOrderByBirthDateDesc(Long farmId, Pageable pageable);

    List<Animal> findAnimalsByBelongsToIdOrderByBirthDateDesc(Long farmId);
}
