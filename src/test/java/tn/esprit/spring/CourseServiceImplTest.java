package tn.esprit.spring;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseServices;

    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        course = new Course(1L, 1, null, null, 100.0f, 10, null);
    }

    @Test
    void testRetrieveAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course));
        List<Course> courses = courseServices.retrieveAllCourses();
        assertEquals(1, courses.size());
        verify(courseRepository, times(1)).findAll();
    }
//
//    @Test
//    void testAddCourse() {
//        when(courseRepository.save(course)).thenReturn(course);
//        Course savedCourse = courseServices.addCourse(course);
//        assertEquals(course, savedCourse);
//        verify(courseRepository, times(1)).save(course);
//    }
//
    @Test
    void testUpdateCourse() {
        when(courseRepository.save(course)).thenReturn(course);
        Course updatedCourse = courseServices.updateCourse(course);
        assertEquals(course, updatedCourse);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testRetrieveCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        Course foundCourse = courseServices.retrieveCourse(1L);
        assertEquals(course, foundCourse);
        verify(courseRepository, times(1)).findById(1L);
    }

}
