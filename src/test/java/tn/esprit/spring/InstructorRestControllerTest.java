package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;
import tn.esprit.spring.controllers.InstructorRestController;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InstructorRestController.class)
class InstructorRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IInstructorServices instructorServices;

    @Test
    void testAddInstructor() throws Exception {
        Instructor instructor = new Instructor(1L, "John", "Doe", null, null);
        when(instructorServices.addInstructor(any(Instructor.class))).thenReturn(instructor);

        mockMvc.perform(post("/instructor/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numInstructor").value(instructor.getNumInstructor()))
                .andExpect(jsonPath("$.firstName").value(instructor.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(instructor.getLastName()));
    }

    @Test
    void testAddAndAssignToInstructor() throws Exception {
        Instructor instructor = new Instructor(1L, "John", "Doe", null, null);
        when(instructorServices.addInstructorAndAssignToCourse(any(Instructor.class), eq(1L))).thenReturn(instructor);

        mockMvc.perform(put("/instructor/addAndAssignToCourse/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numInstructor").value(instructor.getNumInstructor()))
                .andExpect(jsonPath("$.firstName").value(instructor.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(instructor.getLastName()));
    }

    @Test
    void testGetAllInstructors() throws Exception {
        Instructor instructor = new Instructor(1L, "John", "Doe", null, null);
        when(instructorServices.retrieveAllInstructors()).thenReturn(Collections.singletonList(instructor));

        mockMvc.perform(get("/instructor/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numInstructor").value(instructor.getNumInstructor()))
                .andExpect(jsonPath("$[0].firstName").value(instructor.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(instructor.getLastName()));
    }

    @Test
    void testUpdateInstructor() throws Exception {
        Instructor instructor = new Instructor(1L, "John", "Doe", null, null);
        when(instructorServices.updateInstructor(any(Instructor.class))).thenReturn(instructor);

        mockMvc.perform(put("/instructor/updateeeeeeeee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numInstructor\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numInstructor").value(instructor.getNumInstructor()))
                .andExpect(jsonPath("$.firstName").value(instructor.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(instructor.getLastName()));
    }

    @Test
    void testGetById() throws Exception {
        Instructor instructor = new Instructor(1L, "John", "Doe", null, null);
        when(instructorServices.retrieveInstructor(1L)).thenReturn(instructor);

        mockMvc.perform(get("/instructor/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numInstructor").value(instructor.getNumInstructor()))
                .andExpect(jsonPath("$.firstName").value(instructor.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(instructor.getLastName()));
    }
}
