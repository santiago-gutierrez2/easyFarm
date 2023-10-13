package es.udc.paproject.backend.rest.dtos.IssueDTOs;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class IssueDto {

    public interface CreateIssueValidation {}

    public interface UpdateIssueValidation {}

    private Long id;
    private String issueName;
    private String description;
    private Long creationDate;
    private Boolean isDone;
    private Long createdBy;
    private Long assignedTo;

    public IssueDto(Long id, String issueName, String description, Long creationDate, Boolean isDone, Long createdBy, Long assignedTo) {
        this.id = id;
        this.issueName = issueName != null ? issueName.trim() : null;
        this.description = description.trim();
        this.creationDate = creationDate;
        this.isDone = isDone;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull(groups = {CreateIssueValidation.class, UpdateIssueValidation.class})
    @Size(min=1, max=100, groups = {CreateIssueValidation.class, UpdateIssueValidation.class})
    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull(groups = {CreateIssueValidation.class})
    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    @NotNull(groups = {CreateIssueValidation.class})
    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @NotNull(groups = {CreateIssueValidation.class})
    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }
}
