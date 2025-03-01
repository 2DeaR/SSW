package com.example.demo.service;

import com.example.demo.domain.Pet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetService petService;

    @Test
    public void testAddPet() throws Exception {
        Pet pet = new Pet();
        pet.setName("doggie");

        mockMvc.perform(post("/api/v3/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(pet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("doggie"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void testGetPetById() throws Exception {
        Pet pet = new Pet();
        pet.setName("doggie");
        Pet savedPet = petService.addPet(pet);

        mockMvc.perform(get("/api/v3/pet/" + savedPet.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("doggie"))
                .andExpect(jsonPath("$.id").value(savedPet.getId()));
    }

    @Test
    public void testAddPetWithEmptyName() throws Exception {
        Pet pet = new Pet();
        pet.setName("");

        mockMvc.perform(post("/api/v3/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(pet)))
                .andExpect(status().isUnprocessableEntity()) // 422
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value("Name cannot be empty"))
                .andExpect(jsonPath("$.path").value("422")); // Теперь проверяем код 422
    }

    @Test
    public void testGetPetById_NotFound() throws Exception {
        mockMvc.perform(get("/api/v3/pet/999"))
                .andExpect(status().isNotFound()) // 404
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Pet not found"))
                .andExpect(jsonPath("$.path").value("404")); // Теперь проверяем код 404
    }

    @Test
    public void testUpdatePet() throws Exception {
        Pet pet = new Pet();
        pet.setName("doggie");
        Pet savedPet = petService.addPet(pet);

        Pet updatedPet = new Pet();
        updatedPet.setId(savedPet.getId());
        updatedPet.setName("updatedDoggie");

        mockMvc.perform(put("/api/v3/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedPet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updatedDoggie"))
                .andExpect(jsonPath("$.id").value(savedPet.getId()));
    }

    @Test
    public void testDeletePet() throws Exception {
        Pet pet = new Pet();
        pet.setName("doggie");
        Pet savedPet = petService.addPet(pet);

        mockMvc.perform(delete("/api/v3/pet/" + savedPet.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v3/pet/" + savedPet.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatePetWithNoId() throws Exception {
        Pet pet = new Pet();
        pet.setName("doggie");

        mockMvc.perform(put("/api/v3/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(pet)))
                .andExpect(status().isBadRequest()) // 400
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Pet ID is required for update"))
                .andExpect(jsonPath("$.path").value("400")); // Теперь проверяем код 400
    }
}