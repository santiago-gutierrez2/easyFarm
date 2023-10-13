package es.udc.paproject.backend.model.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Issue {

    private Long id;
    private String issueName;
    private String description;
    private LocalDateTime creationDate;
    private Boolean isDone;
    private User createdBy;
    private User assignedTo;

    // empty constructor
    public Issue() {}

    // main constructor
    public Issue(String issueName, String description, Boolean isDone, User createdBy, User assignedTo) {
        this.issueName = issueName;
        this.description = description;
        this.creationDate = LocalDateTime.now().withNano(0);
        this.isDone = isDone;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @JoinColumn(name = "isDone")
    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean done) {
        isDone = done;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy")
    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "assignedTo")
    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }
}
