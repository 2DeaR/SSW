package com.example.demo777.model.payment;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("CHECK")
@Data
@EqualsAndHashCode(callSuper = true)
public class Check extends Payment {
    private String name;
    private String bankId;
}

