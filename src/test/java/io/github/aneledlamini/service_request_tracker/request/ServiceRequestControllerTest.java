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

 
    // Test 1: Test for creating a service request and storing the location as longitude and latitude

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

    // Test 2: Test for creating a service request with a missing category field, expecting a 400 status
    @Test
    void rejectsRequestWithMissingCategory() throws Exception {
        
         String body = 
         """
                {"description":"Deep pothole","latitude":-33.9249,"longitude":18.4241}
         """;
    
         mockMvc.perform(post("/api/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
     }

         

    // Test 3: Test for creating a service request with an out-of-range latitude, expecting a 400 status
    @Test
    void rejectsRequestWithLatOutOFRange() throws Exception {

         String body = 
         """
                {"category":"POTHOLE","description":"Deep pothole","latitude":-100.0,"longitude":18.4241}
         """;
    
         mockMvc.perform(post("/api/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
     }


    // Test 4: Test for creating a service request with unknown category, expecting a 400 status
    @Test
    void rejectsRequestWithUnknownCategory() throws Exception {   
        
         String body = 
         """
                {"category":"SINKHOLE","description":"Deep pothole","latitude":-33.9249,"longitude":18.4241}
         """;
    
         mockMvc.perform(post("/api/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
             
     }


}
