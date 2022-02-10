package com.sparta.api.spartarestapi;

import com.sparta.api.spartarestapi.controller.SpartanController;
import com.sparta.api.spartarestapi.entities.APIKeyEntity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class SpartanControllerTests {

    private SpartanEntity spartanTestEntity;
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
        Mockito.when(mockApiKeyRepository.findAllByAPIKeyIsNotNull()).thenReturn(List.of(apiKeyEntity));
        spartanTestEntity = new SpartanEntity("spartanID","2022-04-15","2022-02-9",4,"Hendrix","Gardner");
        spartanController = new SpartanController(mockSpartanRepository, mockCourseRepository, mockApiKeyRepository);
//        Mockito.when(mockSpartanRepository.findAllByFirstNameIsNotNull()).thenReturn(List.of(spartanTestEntity));
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





}
