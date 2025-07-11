package gr.aueb.cf.managementapp.model;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass


public abstract class AbstractChange {

    public AbstractChange() {
    }

    public AbstractChange(String description, LocalDate dateStarted, LocalDate dateEnded, Long costForWork, Long costForMaterials) {

        this.description = description;
        this.dateStarted = dateStarted;
        this.dateEnded = dateEnded;
        this.costForWork = costForWork;
        this.costForMaterials = costForMaterials;
    }

    @Column(name = "description")
    private String description;

    @Column(name = "date_started")
    private LocalDate dateStarted;

    @Column(name = "date_ended")
    private LocalDate dateEnded;

    @Column(name = "cost_for_work")
    private Long costForWork;

    @Column(name = "cost_for_materials")
    private Long costForMaterials;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    //@UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(unique = true, updatable = false, nullable = false, length = 36)
    private String uuid = UUID.randomUUID().toString();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }



}
