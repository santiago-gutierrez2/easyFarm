package es.udc.paproject.backend.model.entities;

import es.udc.paproject.backend.rest.dtos.MilkingDTOs.MilkingChartDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MilkingDao extends PagingAndSortingRepository<Milking, Long>, CustomizedMilkingDao {

    Slice<Milking> findByAnimalMilkedBelongsToIdOrderByDateDesc(Long farmId, Pageable pageable);

    void deleteAllByAnimalMilkedId(Long animalMilkedId);

    @Query("select new es.udc.paproject.backend.rest.dtos.MilkingDTOs.MilkingChartDto(SUM(m.liters), DATE_FORMAT(m.date, '%Y-%m-%d'), m.animalMilked.id) " +
            "from Milking m left join Animal a on a.id = m.animalMilked.id " +
            "where m.animalMilked.id = :animalId " +
            "group by DATE_FORMAT(m.date, '%Y-%m-%d'), m.animalMilked ")
    List<MilkingChartDto> findMilkingByAnimalMilkedId(Long animalId);
}
