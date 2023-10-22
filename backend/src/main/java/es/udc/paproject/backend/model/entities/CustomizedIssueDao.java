package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public interface CustomizedIssueDao {
    Slice<Issue> find(Long farmId, String issueName, String startDate, String endDate,
                      Boolean isDone, int page, int size);
}
