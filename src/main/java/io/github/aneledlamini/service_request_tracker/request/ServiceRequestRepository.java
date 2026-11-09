package io.github.aneledlamini.service_request_tracker.request;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, UUID> {
    
}
