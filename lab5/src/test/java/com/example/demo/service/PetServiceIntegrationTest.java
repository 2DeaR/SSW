package com.example.demo.service;

import com.example.demo.domain.Category;
import com.example.demo.domain.Pet;
import com.example.demo.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PetServiceIntegrationTest {

    @Autowired
    private PetService petService;

    @Autowired
    private PetRepository petRepository;

    @BeforeEach
    void setUp() {
        petRepository.deleteAll();
    }

    @Test
    void testAddPet() {
        Pet pet = new Pet();
        pet.setName("Fluffy");
        pet.setStatus("available");

        Pet savedPet = petService.addPet(pet);

        assertNotNull(savedPet.getId());
        assertEquals("Fluffy", savedPet.getName());
        assertEquals("available", savedPet.getStatus());
    }

    @Test
    void testUpdatePet() {
        Pet pet = new Pet();
        pet.setName("Buddy");
        pet.setStatus("available");
        Pet savedPet = petService.addPet(pet);

        savedPet.setName("Buddy Updated");
        Pet updatedPet = petService.updatePet(savedPet);

        assertEquals("Buddy Updated", updatedPet.getName());
        assertEquals(savedPet.getId(), updatedPet.getId());
    }

    @Test
    void testGetPetById() {
        Pet pet = new Pet();
        pet.setName("Max");
        pet.setStatus("pending");
        Pet savedPet = petService.addPet(pet);

        Pet foundPet = petService.getPetById(savedPet.getId());

        assertEquals(savedPet.getId(), foundPet.getId());
        assertEquals("Max", foundPet.getName());
    }

    @Test
    void testDeletePet() {
        Pet pet = new Pet();
        pet.setName("Rex");
        Pet savedPet = petService.addPet(pet);

        petService.deletePet(savedPet.getId());

        assertThrows(RuntimeException.class, () -> petService.getPetById(savedPet.getId()));
    }
}