package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.Issue;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;

public interface IssueService {

    void createIssue(Issue issue) throws PermissionException;

    Issue updateIssue(Long id, String issueName, String description, Boolean isDone)
            throws InstanceNotFoundException;

    void deleteIssue(Long userId, Long id) throws InstanceNotFoundException, PermissionException;

    Issue getIssueById(Long userId, Long id) throws InstanceNotFoundException, PermissionException;
}
