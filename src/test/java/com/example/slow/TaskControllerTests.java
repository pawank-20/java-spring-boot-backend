
package com.example.slow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest //@SpringBootTest loads the application for testing.
@AutoConfigureMockMvc
// @WithMockUser //This supplies a fake authenticated user for those tests. It does not test JWT validation itself; we'll test the actual token flow separately.
class TaskControllerTests {

    @Autowired //asks Spring to supply the MockMvc object.
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void createsTaskThroughHttp() throws Exception {
        // mockMvc.perform(...) sends a simulated request.
        mockMvc.perform(post("/tasks").with(user("test-user"))
                .contentType("application/json")
                .content("""
                    {
                        "title": "Test task",
                        "priority": "High"
                    }
                    """))
                    //The triple-quoted strings (""") are Java text blocks. They let you write multiline strings without escaping every quotation mark.
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test task")) //jsonPath("$.title") reads the title field from the returned JSON. The $ means the root of the JSON document.
                .andExpect(jsonPath("$.priority").value("High"));
    }

    @Test
    @WithMockUser
    void rejectsTaskWithoutTitle() throws Exception {
        mockMvc.perform(post("/tasks").with(user("test-user"))
                .contentType("application/json")
                .content("""
                    {
                        "priority": "High"
                    }
                    """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Title is required"));
    }
}

