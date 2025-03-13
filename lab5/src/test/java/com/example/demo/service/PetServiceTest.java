package com.example.demo.service;

import com.example.demo.domain.Pet;
import com.example.demo.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {
    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @Test
    public void testAddPet() {
        Pet pet = new Pet();
        pet.setName("doggie");

        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> {
            Pet p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        Pet result = petService.addPet(pet);

        assertNotNull(result.getId());
        assertEquals("doggie", result.getName());
        verify(petRepository).save(pet);
    }

    @Test
    public void testGetPetById_NotFound() {
        when(petRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> petService.getPetById(1L));
    }
}