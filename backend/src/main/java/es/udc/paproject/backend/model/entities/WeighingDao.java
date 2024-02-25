package es.udc.paproject.backend.model.entities;

import es.udc.paproject.backend.rest.dtos.WeighingDTOs.WeighingChartDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface WeighingDao extends PagingAndSortingRepository<Weighing, Long>, CustomizedWeighingDao {

    Slice<Weighing> findByAnimalWeighedBelongsToIdOrderByDateDesc(Long farmId, Pageable pageable);

    List<Weighing> findWeighingByAnimalWeighedId(Long animalId);

    @Query("SELECT new es.udc.paproject.backend.rest.dtos.WeighingDTOs.WeighingChartDto(SUM(w.kilos), DATE_FORMAT(w.date, '%Y-%m-%d'), f.id) " +
            "FROM Weighing w " +
            "left join Animal a on a.id = w.animalWeighed.id " +
            "left join Farm f on f.id = a.belongsTo.id " +
            "Where f.id = :farmId " +
            "Group by DATE_FORMAT(w.date, '%Y-%m-%d'), f.id ")
    List<WeighingChartDto> getFarmMeatProductionEvolution(Long farmId);
}
