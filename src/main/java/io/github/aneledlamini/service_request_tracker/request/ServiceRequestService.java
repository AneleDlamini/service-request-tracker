package io.github.aneledlamini.service_request_tracker.request;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ServiceRequestService {

    // WGS 84 coordinate reference system (CRS) for latitude and longitude
    private static final GeometryFactory GEOMETRY = new GeometryFactory(new PrecisionModel(), 4326); 

    private final ServiceRequestRepository repository;

    public ServiceRequestService(ServiceRequestRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ServiceRequestResponse create(CreateServiceRequest input) {

        ServiceRequest serviceRequest = new ServiceRequest(
            input.category(),
            input.description(),
            GEOMETRY.createPoint(new org.locationtech.jts.geom.Coordinate(input.longitude(), input.latitude()))
        );
        ServiceRequest savedServiceRequest = repository.save(serviceRequest);
        return ServiceRequestResponse.from(savedServiceRequest);
    }

}
