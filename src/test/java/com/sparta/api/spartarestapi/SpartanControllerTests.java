package com.sparta.api.spartarestapi;

import com.sparta.api.spartarestapi.controller.SpartanController;
import com.sparta.api.spartarestapi.entities.APIKeyEntity;
import com.sparta.api.spartarestapi.entities.CourseEntity;
import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.repositories.APIKeyRepository;
import com.sparta.api.spartarestapi.repositories.CourseRepository;
import com.sparta.api.spartarestapi.repositories.SpartanRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.bind.ValidationException;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SpartanControllerTests {

    private SpartanEntity spartanTestEntity;
    private SpartanEntity updateSpartanTestEntity;
    private CourseEntity courseEntity;
    private SpartanController spartanController;
    private CourseRepository mockCourseRepository;
    private SpartanRepository mockSpartanRepository;
    private APIKeyRepository mockApiKeyRepository;
    private APIKeyEntity apiKeyEntity;

    @BeforeEach
    void setup() {
        mockCourseRepository = Mockito.mock(CourseRepository.class);
        mockSpartanRepository = Mockito.mock(SpartanRepository.class);
        mockApiKeyRepository = Mockito.mock(APIKeyRepository.class);
        apiKeyEntity = new APIKeyEntity("TestKey", "TestApiKey");
        courseEntity = new CourseEntity("kjlsdlk", 1, "Java", "newCourse", true, 1);
        Mockito.when(mockApiKeyRepository.findAllByAPIKeyIsNotNull()).thenReturn(List.of(apiKeyEntity));
        spartanTestEntity = new SpartanEntity("spartanID","2022-04-15","2022-02-09",1,"Hendrix","Gardner");
        updateSpartanTestEntity = new SpartanEntity("spartanID","2022-03-15","2022-02-09",1,"Hendrix","Gardner");
        spartanController = new SpartanController(mockSpartanRepository, mockCourseRepository, mockApiKeyRepository);
    }

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(spartanController);
    }

    @Test
    public void checkGetSpartansNoParameters() throws Exception {
        spartanController.getSpartans(new HashMap<>());
        Mockito.verify(mockSpartanRepository).findAllByFirstNameIsNotNull();
    }

    @Test
    public void checkGetSpartansByFirstNameTest() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("firstName", "hendrix");
        spartanController.getSpartans(params);
        Mockito.verify(mockSpartanRepository).findAllByFirstNameContainsIgnoreCase("hendrix");
    }

    @Test
    public void checkGetSpartansByLastNameTest() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("lastName", "gardner");
        spartanController.getSpartans(params);
        Mockito.verify(mockSpartanRepository).findAllByLastNameContainsIgnoreCase("gardner");
    }

    @Test
    public void checkGetSpartansById() throws Exception {
        Mockito.when(mockSpartanRepository.findById(spartanTestEntity.getId())).thenReturn(Optional.of(spartanTestEntity));
        spartanController.findSpartanById(spartanTestEntity.getId());
        Mockito.verify(mockSpartanRepository).findById(spartanTestEntity.getId());
    }

    @Test
    public void checkAddSpartan() {
        Mockito.when(mockCourseRepository.findByCourseId(spartanTestEntity.getCourseId())).thenReturn(Optional.of(courseEntity));
        Mockito.when(mockCourseRepository.findAllByCourseNameIsNotNull()).thenReturn(Arrays.stream(new CourseEntity[2]).toList());
        try {
            spartanController.addSpartan(spartanTestEntity, apiKeyEntity.getAPIKey());
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Mockito.verify(mockSpartanRepository).save(spartanTestEntity);
    }

    @Test
    public void checkUpdateSpartan() {
        Mockito.when(mockSpartanRepository.findById(spartanTestEntity.getId())).thenReturn(Optional.ofNullable(spartanTestEntity));
        Mockito.when(mockCourseRepository.findByCourseId(spartanTestEntity.getCourseId())).thenReturn(Optional.ofNullable(courseEntity));
        Mockito.when(mockCourseRepository.findAllByCourseNameIsNotNull()).thenReturn(Arrays.stream(new CourseEntity[2]).toList());
        try {
            spartanController.updateSpartan(updateSpartanTestEntity, apiKeyEntity.getAPIKey());
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        Mockito.verify(mockSpartanRepository).save(updateSpartanTestEntity);
    }




    @Test
    public void checkDeleteSpartan(){
        try {
            spartanController.deleteSpartan(spartanTestEntity.getId(), apiKeyEntity.getAPIKey());
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Mockito.verify(mockSpartanRepository).deleteById(spartanTestEntity.getId());
    }
}
