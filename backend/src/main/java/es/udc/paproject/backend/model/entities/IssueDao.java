package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IssueDao extends PagingAndSortingRepository<Issue, Long> {

    Slice<Issue> findByCreatedByFarmId(Long farmId, Pageable pageable);
}
