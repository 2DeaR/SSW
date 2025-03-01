package com.example.demo.repository;

import com.example.demo.domain.Pet;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PetRepository {
    private Map<Long, Pet> petStore = new HashMap<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    public Pet save(Pet pet) {
        if (pet.getId() == null) {
            pet.setId(idGenerator.getAndIncrement());
        }
        petStore.put(pet.getId(), pet);
        return pet;
    }

    public Optional<Pet> findById(Long id) {
        return Optional.ofNullable(petStore.get(id));
    }

    public void delete(Long id) {
        petStore.remove(id);
    }
}