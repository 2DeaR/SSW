package com.example.lab8.model.payment;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("CREDIT")
@Data
@EqualsAndHashCode(callSuper = true)
public class Credit extends Payment {
    private String number;
    private String type;
    private LocalDateTime expDate;
}

