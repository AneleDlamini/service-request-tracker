package io.github.aneledlamini.service_request_tracker;

import org.springframework.boot.SpringApplication;

public class TestServiceRequestTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.from(ServiceRequestTrackerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
