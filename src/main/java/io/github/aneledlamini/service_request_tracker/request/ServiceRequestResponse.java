package io.github.aneledlamini.service_request_tracker.request;

import java.util.UUID;

public record ServiceRequestResponse(UUID id, RequestStatus status, double latitude, double longitude) {
    
    public static ServiceRequestResponse from(ServiceRequest request) {
        return new ServiceRequestResponse(
            request.getId(),
            request.getStatus(),
            request.getLocation().getY(), // Latitude
            request.getLocation().getX() // Longitude
        );
    }
}
