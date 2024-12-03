package com.ouss.productservice.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

@Entity
@Data@AllArgsConstructor@NoArgsConstructor@ToString@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer idProduct;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private boolean disponibility;
}
