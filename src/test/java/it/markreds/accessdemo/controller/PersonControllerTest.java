package it.markreds.accessdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.markreds.accessdemo.domain.Person;
import it.markreds.accessdemo.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PersonControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setup() {
        personRepository.deleteAll();
    }

    @Test
    void shouldReturnAllPersons() throws Exception {
        Person person = personRepository.save(new Person()
                .setFirstName("Marco")
                .setLastName("Rossi")
                .setKeyCode("B177B700A2000000"));
        mockMvc.perform(get("/api/v1/people"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id", is(person.getId().intValue())));
    }

    @Test
    void shouldReturnOnePerson() throws Exception {
        Person person = personRepository.save(new Person()
                .setFirstName("Marco")
                .setLastName("Rossi")
                .setKeyCode("B177B700A2000000"));
        mockMvc.perform(get("/api/v1/people/" + person.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(person.getId().intValue())));
    }

    @Test
    void shouldCreatePerson() throws Exception {
        Person person = new Person()
                .setFirstName("Marco")
                .setLastName("Rossi")
                .setKeyCode("B177B700A2000000");
        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper
                                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                                .writeValueAsString(person)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())));
    }

    @Test
    void shouldUpdatePerson() throws Exception {
        Person person = personRepository.save(new Person()
                .setFirstName("Marco")
                .setLastName("Rossi")
                .setKeyCode("B177B700A2000000"));
        mockMvc.perform(put("/api/v1/people/" + person.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper
                                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                                .writeValueAsString(new Person().setLastName("Bianchi"))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is("Bianchi")));
    }

    @Test
    void shouldDeletePerson() throws Exception {
        Person person = personRepository.save(new Person()
                .setFirstName("Marco")
                .setLastName("Rossi")
                .setKeyCode("B177B700A2000000"));
        mockMvc.perform(delete("/api/v1/people/" + person.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}