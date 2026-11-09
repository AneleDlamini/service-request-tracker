package io.github.aneledlamini.service_request_tracker.request;

import java.time.Instant;
import java.util.*;

import org.locationtech.jts.geom.Point;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity representing a service request.
 */
@Entity
@Table(name = "service_request") 
public class ServiceRequest {
    
    // Table columns
    @Id 
    private UUID id; 

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private Category category;
    
    private String description;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private RequestStatus status;

    @Column (nullable = false)
    private Point location; // Using JTS Point type for spatial data

    @Column (nullable = false)
    private Instant createdAt; // Timestamp for when the request was created

    // For loading from the database (Hibernate), JPA requires a no-argument constructor
    protected ServiceRequest() {
        // Default constructor for JPA
    }

    // Public constructor for creating a new service request
    public ServiceRequest(Category category, String description, Point location) {
        this.id = UUID.randomUUID();
        this.category = category;
        this.description = description;
        this.status = RequestStatus.SUBMITTED; // Default status when creating a new request
        this.location = location;
        this.createdAt = Instant.now(); // Set creation timestamp to current time
    }

    public UUID getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public Point getLocation() {
        return location;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
 
}
