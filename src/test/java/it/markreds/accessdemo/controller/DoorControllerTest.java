package it.markreds.accessdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.markreds.accessdemo.domain.Door;
import it.markreds.accessdemo.domain.Person;
import it.markreds.accessdemo.repository.DoorRepository;
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
class DoorControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DoorRepository doorRepository;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setup() {
        doorRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    void shouldReturnAllDoors() throws Exception {
        Door door = doorRepository.save(new Door()
                .setDisplayName("Main Door")
                .setWorkTime(5)
                .setMacAddress("52:47:09:8f:58:eb"));
        mockMvc.perform(get("/api/v1/doors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id", is(door.getId().intValue())));
    }

    @Test
    void shouldReturnOneDoor() throws Exception {
        Door door = doorRepository.save(new Door()
                .setDisplayName("Main Door")
                .setWorkTime(5)
                .setMacAddress("52:47:09:8f:58:eb"));
        mockMvc.perform(get("/api/v1/doors/" + door.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(door.getId().intValue())));
    }

    @Test
    void shouldCreateDoor() throws Exception {
        Door door = new Door()
                .setDisplayName("Main Door")
                .setWorkTime(5)
                .setMacAddress("52:47:09:8f:58:eb");
        mockMvc.perform(post("/api/v1/doors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper
                                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                                .writeValueAsString(door)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName", is(door.getDisplayName())));
    }

    @Test
    void shouldUpdateDoor() throws Exception {
        Door door = doorRepository.save(new Door()
                .setDisplayName("Main Door")
                .setWorkTime(5)
                .setMacAddress("52:47:09:8f:58:eb"));

        mockMvc.perform(put("/api/v1/doors/" + door.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper
                                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                                .writeValueAsString(new Door().setDisplayName("Garage"))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName", is("Garage")));
    }

    @Test
    void shouldDeleteDoor() throws Exception {
        Door door = doorRepository.save(new Door()
                .setDisplayName("Main Door")
                .setWorkTime(5)
                .setMacAddress("52:47:09:8f:58:eb"));
        mockMvc.perform(delete("/api/v1/doors/" + door.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        assert doorRepository.findAll().isEmpty();
    }

    @Test
    void shouldRequestOpen() throws Exception {
        Door door = doorRepository.save(new Door()
                .setDisplayName("Main Door")
                .setWorkTime(5)
                .setMacAddress("52:47:09:8f:58:eb"));
        Person person = personRepository.save(new Person()
                .setFirstName("Marco")
                .setLastName("Rossi")
                .setKeyCode("B177B700A2000000"));
        mockMvc.perform(get("/api/v1/doors/can-open")
                        .param("door", door.getMacAddress())
                        .param("keycode", person.getKeyCode()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed", is(true)))
                .andExpect(jsonPath("$.workTime", is(5)));
    }

    @Test
    void shouldChangeState() throws Exception {
        Door door = doorRepository.save(new Door()
                .setDisplayName("Main Door")
                .setWorkTime(5)
                .setMacAddress("52:47:09:8f:58:eb"));
        mockMvc.perform(get("/api/v1/doors/state-changed")
                        .param("door", door.getMacAddress())
                        .param("open", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemType", is("DOOR_OPEN")));
    }
}