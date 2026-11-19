package io.github.aneledlamini.service_request_tracker.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.utility.TestcontainersConfiguration;

import com.jayway.jsonpath.JsonPath;

// Purpose: This class is a test configuration for the ServiceRequestController
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public class ServiceRequestControllerTest {
    
    @Autowired MockMvc mockMvc;
    @Autowired JdbcTemplate jdbc;

    @Test 
    void createsRequestAndStoresLocationAsLonLat() throws Exception {
       String body = 
       """
            {"category":"POTHOLE","description":"Deep pothole","latitude":-33.9249,"longitude":18.4241}
       """;

       MvcResult result = mockMvc.perform(post("/api/requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.status").value("SUBMITTED"))
            .andReturn();
        

        // Verifying that the location is stored as a POINT in the database
        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        String sql = "SELECT category, status, ST_X(location) AS lon, ST_Y(location) AS lat FROM service_request";

        Double lon = jdbc.queryForObject("SELECT ST_X(location) AS lon FROM service_request WHERE id = ?::uuid", Double.class, id);
        Double lat = jdbc.queryForObject("SELECT ST_Y(location) AS lat FROM service_request WHERE id = ?::uuid", Double.class, id);
        assertThat(lon).isEqualTo(18.4241);
        assertThat(lat).isEqualTo(-33.9249);
    }
    


}
