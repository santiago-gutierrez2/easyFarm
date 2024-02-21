package es.udc.paproject.backend.rest.controllers;

import es.udc.paproject.backend.model.entities.Issue;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.IssueService;
import es.udc.paproject.backend.model.services.UserService;
import es.udc.paproject.backend.rest.dtos.BlockDto;
import es.udc.paproject.backend.rest.dtos.IssueDTOs.IssueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static es.udc.paproject.backend.rest.dtos.IssueDTOs.IssueConversor.*;

@RestController
@RequestMapping("/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @PostMapping("/createIssue")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createIssue(@RequestAttribute Long userId,
        @Validated({IssueDto.CreateIssueValidation.class}) @RequestBody IssueDto issueDto)
        throws PermissionException, InstanceNotFoundException {

        User user = userService.loginFromId(userId);

        Issue issue = toIssue(issueDto);
        issue.setCreatedBy(user);
        issue.setAssignedTo(userService.loginFromId(issueDto.getAssignedTo()));

        issueService.createIssue(issue);

    }

    @PutMapping("/{id}")
    public IssueDto updateIssue(@RequestAttribute Long userId, @PathVariable Long id,
        @Validated({IssueDto.UpdateIssueValidation.class}) @RequestBody IssueDto issueDto)
        throws InstanceNotFoundException, PermissionException {

        if (!userId.equals(issueDto.getCreatedBy())) {
            throw new PermissionException();
        }

        return toIssueDto(issueService.updateIssue(id, issueDto.getIssueName(), issueDto.getDescription(), issueDto.getDone()));
    }

    @PutMapping("/{id}/setAsDone")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setIssueAsDone(@RequestAttribute Long userId, @PathVariable Long id) throws InstanceNotFoundException {
        issueService.setIssueAsDone(userId, id);
    }

    @DeleteMapping("/{id}/deleteIssue")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@RequestAttribute Long userId, @PathVariable Long id) throws InstanceNotFoundException, PermissionException{
        issueService.deleteIssue(userId, id);
    }

    @GetMapping("/{id}")
    public IssueDto getIssueById(@RequestAttribute Long userId, @PathVariable Long id) throws PermissionException, InstanceNotFoundException {
        Issue issue = issueService.getIssueById(userId, id);
        return toIssueDto(issue);
    }

    @GetMapping("/allIssues")
    public BlockDto<IssueDto> getAllIssues(@RequestAttribute Long userId, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue = "5") int size,
                                           @RequestParam(required = false) String issueName, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,
                                           @RequestParam(required = false) Boolean isDone) throws InstanceNotFoundException {

        Block<Issue> issueBlock = issueService.getAllIssues(userId, issueName, startDate, endDate, isDone, page, size);

        return new BlockDto<>(toIssueDtos(issueBlock.getItems()) , issueBlock.getExistMoreItems());
    }

    @GetMapping("/myActiveIssues")
    public List<IssueDto> findMyActiveIssues(@RequestAttribute Long userId) throws InstanceNotFoundException {
        List<Issue> issues = issueService.myActiveIssues(userId);
        return toIssueDtos(issues);
    }
}
