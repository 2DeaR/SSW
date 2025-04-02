package com.example.demo777.model;

import com.example.demo777.model.value.Measurement;
import com.example.demo777.model.value.Weight;
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

    // Добавляем остальные поля по диаграмме
    private String name;
    private BigDecimal price;
}