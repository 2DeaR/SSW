package com.example.lab8.model.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {
    @Column(name = "measurement_name")
    private String name;

    @Column(name = "measurement_symbol")
    private String symbol;
}