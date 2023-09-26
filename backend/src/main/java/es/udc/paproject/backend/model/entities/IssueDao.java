package es.udc.paproject.backend.model.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface IssueDao extends PagingAndSortingRepository<Issue, Long> {
}
