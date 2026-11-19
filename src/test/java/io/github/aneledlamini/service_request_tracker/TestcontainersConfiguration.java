package io.github.aneledlamini.service_request_tracker;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	PostgreSQLContainer postgresContainer() {
		// Point to the official PostGIS image for PostgreSQL with PostGIS extension
		DockerImageName postgis = DockerImageName.parse("postgis/postgis:15-3.5-alpine")
									.asCompatibleSubstituteFor("postgres");
		return new PostgreSQLContainer(postgis);
	}

}
	