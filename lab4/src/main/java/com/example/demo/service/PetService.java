package com.example.demo.service;

import com.example.demo.domain.Pet;
import com.example.demo.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetService {
    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet addPet(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet updatePet(Pet pet) {
        if (pet.getId() == null) {
            throw new IllegalArgumentException("Pet ID is required for update");
        }
        return petRepository.findById(pet.getId())
                .map(existingPet -> petRepository.save(pet))
                .orElseThrow(() -> new RuntimeException("Pet not found"));
    }

    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
    }

    public void updatePetWithForm(Long petId, String name, String status) {
        Pet pet = getPetById(petId);
        if (name != null) pet.setName(name);
        if (status != null) pet.setStatus(status);
        petRepository.save(pet);
    }

    public void deletePet(Long petId) {
        petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        petRepository.delete(petId);
    }
}