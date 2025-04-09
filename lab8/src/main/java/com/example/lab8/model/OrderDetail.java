    package com.example.lab8.model;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.Data;

    @Entity
    @Table(name = "order_detail")
    @Data
    public class OrderDetail {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "quantity_value")
        private Integer quantityValue;

        @Column(name = "tax_status")
        private String taxStatus;

        @ManyToOne
        @JoinColumn(name = "order_id")
        @JsonIgnore
        private Order order;

        @ManyToOne
        @JoinColumn(name = "item_id")
        private Item item;
    }