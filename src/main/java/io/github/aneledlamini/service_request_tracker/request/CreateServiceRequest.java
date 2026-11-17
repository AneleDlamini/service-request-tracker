package io.github.aneledlamini.service_request_tracker.request;

import jakarta.validation.constraints.*;

// Record representing the data required to create a new service request.
public record CreateServiceRequest(
    @NotNull Category category,
    @Size(max = 2000) String description,
    @NotNull @DecimalMin(value = "-90") @DecimalMax(value = "90") Double latitude,
    @NotNull @DecimalMin(value = "-180") @DecimalMax(value = "180") Double longitude
) {}