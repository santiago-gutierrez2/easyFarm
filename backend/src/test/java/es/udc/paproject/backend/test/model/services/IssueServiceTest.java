package es.udc.paproject.backend.test.model.services;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.IssueService;
import es.udc.paproject.backend.model.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class IssueServiceTest {

    private final Long NON_EXISTENT_ID = (long) -1;

    @Autowired
    private IssueService issueService;
    @Autowired
    private UserService userService;
    @Autowired
    private FarmDao farmDao;
    @Autowired
    private UserDao userDao;

    private User createEmployee(String userName) throws DuplicateInstanceException {
        Farm farm = farmDao.findById(1L).get();
        User user = new User(userName,"55043207K", "17000058787", "password", "firstName",
                "lastName", userName + "@" + userName + ".com", farm);
        userService.signUp(user);
        return user;
    }

    private Issue createIssue(String name, User employee) {
        User creator = userDao.findById(1L).get();
        return new Issue(name, "TEST", false, creator, employee);
    }

    @Test
    public void testCreateIssue() throws PermissionException, InstanceNotFoundException, DuplicateInstanceException {
        User employee = createEmployee("employeeTest");
        Issue issue = createIssue("IssueTest", employee);
        //entityManager.flush();

        // create Issue
        issueService.createIssue(issue);

        Issue createdIssue = issueService.getIssueById(1L, issue.getId());

        assertEquals(createdIssue, issue);
    }

    @Test
    public void testCreateIssueWithNoAdminUser() throws DuplicateInstanceException {
        User employee = createEmployee("employeeTest");
        Issue issue = createIssue("IssueTest", employee);
        issue.setCreatedBy(employee);

        assertThrows(PermissionException.class, () -> issueService.createIssue(issue));
    }

    @Test
    public void testUpdateIssue() throws PermissionException, InstanceNotFoundException, DuplicateInstanceException {
        User employee = createEmployee("employeeTest");
        Issue issue = createIssue("IssueTest", employee);

        // create Issue
        issueService.createIssue(issue);
        // update issue
        issue.setIssueName("X");
        issue.setDescription("X");
        issueService.updateIssue(issue.getId(), "X", "X", false);

        Issue updatedIssue = issueService.getIssueById(1L, issue.getId());

        assertEquals(issue, updatedIssue);
    }

    @Test
    public void testUpdateNoExistingIssue() {
        assertThrows(InstanceNotFoundException.class,
                () -> issueService.updateIssue(NON_EXISTENT_ID, "X", "X", false));
    }

    @Test
    public void testDeleteIssue() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
        User employee = createEmployee("employeeTest");
        Issue issue = createIssue("IssueTest", employee);

        // create Issue
        issueService.createIssue(issue);
        // delete issue
        issueService.deleteIssue(1L, issue.getId());

        assertThrows(InstanceNotFoundException.class,
                () -> issueService.getIssueById(1L, issue.getId()));
    }

    @Test
    public void testDeleteNoExistingIssue() {
        assertThrows(InstanceNotFoundException.class,
                () -> issueService.deleteIssue(1L, NON_EXISTENT_ID));
    }

    @Test
    public void testDeleteIssueCreatedByOtherUser() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
        User employee = createEmployee("employeeTest");
        Issue issue = createIssue("IssueTest", employee);

        // create Issue
        issueService.createIssue(issue);

        assertThrows(PermissionException.class,
                () -> issueService.deleteIssue(NON_EXISTENT_ID, issue.getId()));
    }

    @Test
    public void testGetIssueById() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
        User employee = createEmployee("employeeTest");
        Issue issue = createIssue("IssueTest", employee);

        // create Issue
        issueService.createIssue(issue);
        // get Issue
        Issue createdIssue = issueService.getIssueById(1L, issue.getId());

        assertEquals(issue, createdIssue);
    }

    @Test
    public void testGetNonExistingIssue() {
        assertThrows(InstanceNotFoundException.class,
                () -> issueService.getIssueById(1L, NON_EXISTENT_ID));
    }

    @Test
    public void testGetIssueWithAUserNoAssociatedToIssue() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
        User employee = createEmployee("employeeTest");
        Issue issue = createIssue("IssueTest", employee);

        // create Issue
        issueService.createIssue(issue);

        assertThrows(PermissionException.class,
                () -> issueService.getIssueById(NON_EXISTENT_ID, issue.getId()));
    }

    @Test
    public void testGetAllIssues() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
        User employee = createEmployee("employeeTest");
        Issue issue = createIssue("IssueTest", employee);
        Issue issue1 = createIssue("IssueTest1", employee);

        // create Issue
        issueService.createIssue(issue);
        issueService.createIssue(issue1);

        Block<Issue> issues = issueService.getAllIssues(1L, null, null, null, null, 0, 10);

        assertEquals(2, issues.getItems().size());
    }

    @Test
    public void testGetAllIssuesWithFilter() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
        User employee = createEmployee("employeeTest");
        Issue issue = createIssue("IssueTest", employee);
        Issue issue1 = createIssue("IssueTest1", employee);

        // create Issue
        issueService.createIssue(issue);
        issueService.createIssue(issue1);

        Block<Issue> issues = issueService.getAllIssues(1L, "Issue", null, null, false, 0, 10);

        assertEquals(2, issues.getItems().size());
    }

    @Test
    public void testGetAllIssuesWithNoAssociatedUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> issueService.getAllIssues(NON_EXISTENT_ID, null, null, null, null, 0, 10));
    }

    @Test
    public void testMyActiveIssues() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
        User employee = createEmployee("employeeTest");
        Issue issue = createIssue("IssueTest", employee);
        Issue issue1 = createIssue("IssueTest1", employee);

        // create Issue
        issueService.createIssue(issue);
        issueService.createIssue(issue1);

        List<Issue> issues = issueService.myActiveIssues(employee.getId());

        assertEquals(2, issues.size());
    }

    @Test
    public void testMyActiveIssuesWithNonExistingUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> issueService.myActiveIssues(NON_EXISTENT_ID));
    }

    @Test
    public void testSetIssueAsDone() throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {
        User employee = createEmployee("employeeTest");
        Issue issue = createIssue("IssueTest", employee);

        // create Issue
        issueService.createIssue(issue);
        // set issue as done
        issueService.setIssueAsDone(employee.getId(), issue.getId());

        assertTrue(issue.getIsDone());
    }

    @Test
    public void testSetAsDoneWithNonExistingUser() {
        assertThrows(InstanceNotFoundException.class,
                () -> issueService.setIssueAsDone(NON_EXISTENT_ID, 1L));
    }

    @Test
    public void testSetAsDoneWithNonExistingIssue() {
        assertThrows(InstanceNotFoundException.class,
                () -> issueService.setIssueAsDone(1L, NON_EXISTENT_ID));
    }
}
