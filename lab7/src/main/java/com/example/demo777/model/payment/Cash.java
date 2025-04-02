package com.example.demo777.model.payment;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("CASH")
@Data
@EqualsAndHashCode(callSuper = true)
public class Cash extends Payment {
    private float cashTendered;
}