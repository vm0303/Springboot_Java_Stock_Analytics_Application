package io.endeavour.stocks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.endeavour.stocks.UnitTestBase;
import io.endeavour.stocks.entity.crud.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;


@AutoConfigureMockMvc
class CrudControllerTest extends UnitTestBase
{

    private static final Logger LOGGER = LoggerFactory.getLogger(CrudControllerTest.class);
    @Autowired
    MockMvc mockMvc;

    //Object Mapper can convert a JSON to an object to work on.
    ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    ThreadLocal<Person> personThreadLocal = new ThreadLocal<>();

    @BeforeEach
    public void test_CreatePerson() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/crud/person")
                .content(getJson("test-data/create-person.json"))
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        Person person = objectMapper.readValue(contentAsString, Person.class);
        LOGGER.info("Person object after creation is {} ", person);
        personThreadLocal.set(person);
    }

@Test
    public void test_Person404() throws Exception {
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/crud/person?personID=100");
    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isNotFound());
}

@Test
    public void test_PersonExists() throws Exception {
     MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/crud/person?personID=420");
   MvcResult mvcResult = mockMvc.perform(requestBuilder)
                   .andExpect(MockMvcResultMatchers.status().isOk())
                           .andReturn();
    String contentAsString = mvcResult.getResponse().getContentAsString();
    Person person = objectMapper.readValue(contentAsString, Person.class);
    assertEquals(420, person.getPersonID());

}

@Test
public void test_UpdatePerson() throws Exception
{
    Person person = personThreadLocal.get();
    LOGGER.info("Person info before update is {}", person);
    personThreadLocal.get().setFirstName("");
    person.setLastName("Bones");
    String jsonString = objectMapper.writeValueAsString(person);

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/crud/person?personID=" + person.getPersonID())
            .content(jsonString)
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());

    MockHttpServletRequestBuilder requestBuilderGet = MockMvcRequestBuilders.get("/crud/person?personID=" + person.getPersonID());
    MvcResult mvcResult = mockMvc.perform(requestBuilderGet)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String contentAsString = mvcResult.getResponse().getContentAsString();
    Person updatedPerson = objectMapper.readValue(contentAsString, Person.class);
    LOGGER.info("Person info after update is {}", updatedPerson);
    assertEquals("Grace", updatedPerson.getFirstName());

}

    @Test
    public void test_DeletePerson() throws Exception
    {
        Person person = personThreadLocal.get();
        LOGGER.info("Person object to be deleted is {}", person);
        MockHttpServletRequestBuilder deleteRequestBuilder = MockMvcRequestBuilders.delete("/crud/person?personID=" + person.getPersonID());
        mockMvc.perform(deleteRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());

    }



static String getJson(String filePath) throws IOException {
    Resource resource = new ClassPathResource(filePath);
    return Files.readString(resource.getFile().toPath());
}
}