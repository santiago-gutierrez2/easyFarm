package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.Issue;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface IssueService {

    void createIssue(Issue issue) throws PermissionException;

    Issue updateIssue(Long id, String issueName, String description, Boolean isDone)
            throws InstanceNotFoundException;

    void deleteIssue(Long userId, Long id) throws InstanceNotFoundException, PermissionException;

    Issue getIssueById(Long userId, Long id) throws InstanceNotFoundException, PermissionException;

    Block<Issue> getAllIssues(Long userId, String issueName, String startDate, String endTime,
                              Boolean isDone, int page, int size) throws InstanceNotFoundException;

    List<Issue> myActiveIssues(Long userId) throws InstanceNotFoundException;

    void setIssueAsDone(Long userId, Long id) throws InstanceNotFoundException;
}
