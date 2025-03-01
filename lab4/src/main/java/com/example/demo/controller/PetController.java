package com.example.demo.controller;

import com.example.demo.domain.Pet;
import com.example.demo.service.PetService;
import jakarta.validation.Valid;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v3")
public class PetController {
    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PutMapping("/pet")
    public ResponseEntity<Pet> updatePet(@Valid @RequestBody Pet pet) {
        return ResponseEntity.ok(petService.updatePet(pet));
    }

    @PostMapping("/pet")
    public ResponseEntity<Pet> addPet(@Valid @RequestBody Pet pet) {
        return ResponseEntity.ok(petService.addPet(pet));
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long petId) {
        return ResponseEntity.ok(petService.getPetById(petId));
    }

    @PostMapping("/pet/{petId}")
    public ResponseEntity<Void> updatePetWithForm(
            @PathVariable Long petId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status) {
        petService.updatePetWithForm(petId, name, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/pet/{petId}")
    public ResponseEntity<Void> deletePet(
            @PathVariable Long petId,
            @RequestHeader(value = "api_key", required = false) String apiKey) {
        petService.deletePet(petId);
        return ResponseEntity.ok().build();
    }




    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "Validation failed";
        ErrorResponse errorResponse = new ErrorResponse(
                "Validation Error",
                errorMessage,
                String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()) // 422
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Not Found",
                ex.getMessage(),
                String.valueOf(HttpStatus.NOT_FOUND.value()) // 404
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Bad Request",
                ex.getMessage(),
                String.valueOf(HttpStatus.BAD_REQUEST.value()) // 400
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(TypeMismatchException ex) {
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(), ex.getPropertyName(), ex.getRequiredType().getSimpleName());
        ErrorResponse errorResponse = new ErrorResponse(
                "Bad Request",
                message,
                String.valueOf(HttpStatus.BAD_REQUEST.value()) // 400
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}