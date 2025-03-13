package com.example.demo.service;

import com.example.demo.domain.Category;
import com.example.demo.domain.Pet;
import com.example.demo.domain.Tag;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.PetRepository;
import com.example.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Autowired
    public PetService(PetRepository petRepository,
                      CategoryRepository categoryRepository,
                      TagRepository tagRepository) {
        this.petRepository = petRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public Pet addPet(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet updatePet(Pet pet) {
        if (pet.getId() == null) {
            throw new IllegalArgumentException("Pet ID is required for update");
        }
        if (!petRepository.existsById(pet.getId())) {
            throw new RuntimeException("Pet not found");
        }
        return petRepository.save(pet);
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
        if (!petRepository.existsById(petId)) {
            throw new RuntimeException("Pet not found");
        }
        petRepository.deleteById(petId);
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Tag addTag(Tag tag) {
        return tagRepository.save(tag);
    }
}