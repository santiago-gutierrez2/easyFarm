package es.udc.paproject.backend.rest.dtos.IssueDTOs;

import es.udc.paproject.backend.model.entities.Issue;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class IssueConversor {

    private IssueConversor() {}

    public final static IssueDto toIssueDto(Issue issue) {
        return new IssueDto(issue.getId(), issue.getIssueName(), issue.getDescription(),
                toMillis(issue.getCreationDate()), issue.getIsDone(), issue.getCreatedBy().getId(), issue.getAssignedTo().getId());
    }

    public final static Issue toIssue(IssueDto issueDto) {
        return new Issue(issueDto.getIssueName(), issueDto.getDescription(), issueDto.getDone(), null, null);
    }

    private static long toMillis(LocalDateTime date) {
        return date.truncatedTo(ChronoUnit.MINUTES).atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }
}
