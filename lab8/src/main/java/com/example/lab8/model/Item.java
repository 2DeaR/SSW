package com.example.lab8.model;

import com.example.lab8.model.value.Measurement;
import com.example.lab8.model.value.Weight;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Embedded
    private Weight shippingWeight;

    @Embedded
    private Measurement measurement;

    private String name;
    private BigDecimal price;
}