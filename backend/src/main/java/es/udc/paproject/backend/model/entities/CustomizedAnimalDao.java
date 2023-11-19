package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Slice;

public interface CustomizedAnimalDao {
    Slice<Animal> find(Long farmId, String name, Long identificationNumber, String startDate, String endDate,
                       Boolean isMale, int page, int size);
}
