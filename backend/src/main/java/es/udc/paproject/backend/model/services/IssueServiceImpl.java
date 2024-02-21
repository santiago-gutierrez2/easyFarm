package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import org.apache.catalina.WebResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IssueServiceImpl implements IssueService{

    @Autowired
    private IssueDao issueDao;
    @Autowired
    private UserDao userDao;

    @Override
    public void createIssue(Issue issue) throws PermissionException {

        if (issue.getCreatedBy().getRole() != User.RoleType.ADMIN) {
            throw new PermissionException();
        }

        issueDao.save(issue);
    }

    @Override
    public Issue updateIssue(Long id, String issueName, String description, Boolean isDone)
        throws InstanceNotFoundException {

        Optional<Issue> OptionalIssue = issueDao.findById(id);

        if (OptionalIssue.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.issue", id);
        }

        Issue issue = OptionalIssue.get();
        issue.setIssueName(issueName); // cannot be null
        issue.setDescription(description);
        issue.setIsDone(isDone);

        issueDao.save(issue);

        return issue;
    }

    @Override
    public void deleteIssue(Long userId, Long id) throws InstanceNotFoundException , PermissionException{

        Optional<Issue> OptionalIssue = issueDao.findById(id);
        Optional<User> optionalUser = userDao.findById(userId);

        if (OptionalIssue.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.issue", id);
        } else if (optionalUser.isEmpty() || (!OptionalIssue.get().getCreatedBy().getId().equals(optionalUser.get().getId()))) {
            throw new PermissionException();
        }

        issueDao.delete(OptionalIssue.get());
    }

    @Override
    public Issue getIssueById(Long userId, Long id) throws InstanceNotFoundException, PermissionException {

        Optional<Issue> optionalIssue = issueDao.findById(id);
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalIssue.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.issue", id);
        } else if (optionalUser.isEmpty() || ( !optionalIssue.get().getCreatedBy().getId().equals(optionalUser.get().getId()) &&
                    !optionalIssue.get().getAssignedTo().getId().equals(optionalUser.get().getId()) )) {
            throw new PermissionException();
        }

        return optionalIssue.get();
    }

    @Override
    public Block<Issue> getAllIssues(Long userId, String issueName, String startDate, String endDate,
                                     Boolean isDone, int page, int size) throws InstanceNotFoundException {
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        User user = optionalUser.get();

        Farm farm = user.getFarm();

        Slice<Issue> issuesSlice;
        if (issueName != null || startDate != null || endDate != null || isDone != null) {
            issuesSlice = issueDao.find(farm.getId(), issueName != null ? issueName.trim() : null, startDate, endDate, isDone, page, size);
        } else {
            issuesSlice = issueDao.findByCreatedByFarmIdOrderByCreationDateDesc(farm.getId(), PageRequest.of(page, size));
        }

        List<Issue> issueList = issuesSlice.getContent();

        return new Block<>(issueList, issuesSlice.hasNext());


    }

    @Override
    public List<Issue> myActiveIssues(Long userId) throws InstanceNotFoundException {
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        User user = optionalUser.get();

        return issueDao.findAllByAssignedToIdAndIsDoneFalse(user.getId());
    }

    @Override
    public void setIssueAsDone(Long userId, Long id) throws InstanceNotFoundException {
        Optional<User> optionalUser = userDao.findById(userId);
        Optional<Issue> optionalIssue = issueDao.findById(id);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        } else if (optionalIssue.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.issue", id);
        }

        Issue issue = optionalIssue.get();
        issue.setIsDone(true);

        issueDao.save(issue);

    }


}
