package cynbit_assistant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_leads")
public class ClientLead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(name = "service_interest")
    private String serviceInterest;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    public ClientLead() {
    }

    public ClientLead(String fullName, String email, String serviceInterest) {
        this.fullName = fullName;
        this.email = email;
        this.serviceInterest = serviceInterest;
        this.submittedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        this.submittedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServiceInterest() {
        return serviceInterest;
    }

    public void setServiceInterest(String serviceInterest) {
        this.serviceInterest = serviceInterest;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
}