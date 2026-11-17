package io.github.aneledlamini.service_request_tracker.request;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/api/requests")
public class ServiceRequestController {

    private final ServiceRequestService service;

    public ServiceRequestController(ServiceRequestService service) {
        this.service = service;
    }

    // Purpose: Handle POST requests to create a new service request.
    @PostMapping
    public ResponseEntity<ServiceRequestResponse> create(@Valid @RequestBody CreateServiceRequest input) {
        ServiceRequestResponse response = service.create(input);
        String locationHeader = "/api/requests/" + response.id();
        return ResponseEntity.created(URI.create(locationHeader)).body(response); // Return 201 Created with Location header and response body
    }
    

}
