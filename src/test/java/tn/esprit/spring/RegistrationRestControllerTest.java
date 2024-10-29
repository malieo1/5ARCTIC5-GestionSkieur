package tn.esprit.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.controllers.RegistrationRestController;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.services.IRegistrationServices;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegistrationRestController.class)
class RegistrationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRegistrationServices registrationServices;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddAndAssignToSkier() throws Exception {
        Registration registration = new Registration();
        registration.setNumWeek(1);

        when(registrationServices.addRegistrationAndAssignToSkier(any(Registration.class), any(Long.class)))
                .thenReturn(registration);

        mockMvc.perform(put("/registration/addAndAssignToSkier/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isOk());
    }
}
