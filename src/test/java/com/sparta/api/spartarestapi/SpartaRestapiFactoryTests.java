package com.sparta.api.spartarestapi;

import com.sparta.api.spartarestapi.factories.SpartansFactory;
import com.sparta.api.spartarestapi.repositories.SpartanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.bind.ValidationException;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest
public class SpartaRestapiFactoryTests {

    private SpartanRepository spartanRepository;
    private SpartansFactory spartansFactory;

    @BeforeEach
    void setup() {
        spartanRepository = Mockito.mock(SpartanRepository.class);
        spartansFactory = new SpartansFactory(spartanRepository);
    }


    @Test
    @DisplayName("Test methods call order for first name")
    void firstNameMethodsCallTests() throws ValidationException {
        Map<String, String> spartanParameters = new HashMap<>();
        spartanParameters.put("firstName", "testName");
        spartanParameters.put("active", "true");
        spartansFactory.getSpartans(spartanParameters);
        InOrder inOrder = Mockito.inOrder(spartanRepository);
        inOrder.verify(spartanRepository).findAllByFirstNameIsNotNull();
        inOrder.verify(spartanRepository).findAllByFirstNameContainsIgnoreCase(spartanParameters.get("firstName"));
    }

    @Test
    @DisplayName("Test methods call order for last name")
    void lastNameMethodsCallTest() throws ValidationException {
        Map<String, String> spartanParameters = new HashMap<>();
        spartanParameters.put("lastName", "testName");
        spartanParameters.put("active", "true");
        spartansFactory.getSpartans(spartanParameters);
        InOrder inOrder = Mockito.inOrder(spartanRepository);
        inOrder.verify(spartanRepository).findAllByFirstNameIsNotNull();
        inOrder.verify(spartanRepository).findAllByLastNameContainsIgnoreCase(spartanParameters.get("lastName"));

    }

    @Test
    @DisplayName("Test methods call order for first name and last name")
    void firstNameAndLastNameMethodsCallTest() throws ValidationException {
        Map<String, String> spartanParameters = new HashMap<>();
        spartanParameters.put("firstName", "testName");
        spartanParameters.put("lastName", "testName");
        spartanParameters.put("active", "true");
        spartansFactory.getSpartans(spartanParameters);
        InOrder inOrder = Mockito.inOrder(spartanRepository);
        inOrder.verify(spartanRepository).findAllByFirstNameIsNotNull();
        inOrder.verify(spartanRepository).findAllByFirstNameContainsIgnoreCase(spartanParameters.get("firstName"));
        inOrder.verify(spartanRepository).findAllByLastNameContainsIgnoreCase(spartanParameters.get("lastName"));

    }
}
