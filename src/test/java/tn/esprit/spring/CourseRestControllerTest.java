package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tn.esprit.spring.controllers.CourseRestController;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.services.ICourseServices;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseRestController.class)
class CourseRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICourseServices courseServices;

    @Test
    void testGetAllCourses() throws Exception {
        Course course = new Course(1L, 1, TypeCourse.COLLECTIVE_CHILDREN, Support.SKI, 100.0f, 10, null);
        when(courseServices.retrieveAllCourses()).thenReturn(Collections.singletonList(course));

        mockMvc.perform(MockMvcRequestBuilders.get("/course/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numCourse").value(course.getNumCourse()))
                .andExpect(jsonPath("$[0].level").value(course.getLevel()));
    }
//
//    @Test
//    void testAddCourse() throws Exception {
//        Course course = new Course(1L, 1, TypeCourse.COLLECTIVE_CHILDREN, Support.SKI, 100.0f, 10, null);
//        when(courseServices.addCourse(any(Course.class))).thenReturn(course);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/course/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"level\":1,\"typeCourse\":\"SOME_TYPE\",\"support\":\"SOME_SUPPORT\",\"price\":100.0,\"timeSlot\":10}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.numCourse").value(course.getNumCourse()))
//                .andExpect(jsonPath("$.level").value(course.getLevel()))
//                .andExpect(jsonPath("$.typeCourse").value(course.getTypeCourse().toString()))
//                .andExpect(jsonPath("$.support").value(course.getSupport().toString()))
//                .andExpect(jsonPath("$.price").value(course.getPrice()))
//                .andExpect(jsonPath("$.timeSlot").value(course.getTimeSlot()));
//    }
//
//    @Test
//    void testGetCourseById() throws Exception {
//        Course course = new Course(1L, 1, TypeCourse.COLLECTIVE_CHILDREN, Support.SKI, 100.0f, 10, null);
//        when(courseServices.retrieveCourse(1L)).thenReturn(course);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/course/get/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.numCourse").value(course.getNumCourse()))
//                .andExpect(jsonPath("$.level").value(course.getLevel()));
//    }
}
