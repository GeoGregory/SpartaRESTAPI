package com.sparta.api.spartarestapi;

import com.sparta.api.spartarestapi.controller.CourseController;
import com.sparta.api.spartarestapi.entities.APIKeyEntity;
import com.sparta.api.spartarestapi.entities.CourseEntity;
import com.sparta.api.spartarestapi.repositories.APIKeyRepository;
import com.sparta.api.spartarestapi.repositories.CourseRepository;
import com.sparta.api.spartarestapi.repositories.SpartanRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CourseControllerTests {

    private CourseRepository mockCourseRepository;
    private SpartanRepository mockSpartanRepository;
    private APIKeyRepository mockApiKeyRepository;
    private CourseEntity testCourse1;
    private CourseEntity testCourse2;
    private APIKeyEntity apiKeyEntity;
    private CourseController courseController;

    @BeforeEach
    void setup(){
        mockCourseRepository = Mockito.mock(CourseRepository.class);
        mockSpartanRepository = Mockito.mock(SpartanRepository.class);
        mockApiKeyRepository = Mockito.mock(APIKeyRepository.class);
        apiKeyEntity = new APIKeyEntity("TestKey", "TestApiKey");
        Mockito.when(mockApiKeyRepository.findAllByAPIKeyIsNotNull()).thenReturn(List.of(apiKeyEntity));
        testCourse1 = new CourseEntity("testsCourseId", 1, "testCourse", "test", true, 8);
        testCourse2 = new CourseEntity("testsCourseId", 11, "testCourse", "test", true, 8);
        courseController = new CourseController(mockCourseRepository, mockSpartanRepository, mockApiKeyRepository);
    }

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(courseController);
    }

    @Test
    public void saveNewCourse() throws ValidationException {
        courseController.addCourse(testCourse2, apiKeyEntity.getAPIKey());
        Mockito.verify(mockCourseRepository).save(testCourse2);
    }

    @Test
    public void getCoursesNoParams(){
        courseController.getCourses(null);
        Mockito.verify(mockCourseRepository).findAllByCourseNameIsNotNull();
    }

    @Test
    public void getCoursesNameParameter(){
        courseController.getCourses("test");
        Mockito.verify(mockCourseRepository).findAllByCourseNameContainsIgnoreCase("test");
    }

    @Test
    public void getCourseById(){
        Mockito.when(mockCourseRepository.findByCourseId(1)).thenReturn(Optional.of(testCourse1));
        courseController.findCourseById(1);
        Mockito.verify(mockCourseRepository).findByCourseId(1);
    }

}