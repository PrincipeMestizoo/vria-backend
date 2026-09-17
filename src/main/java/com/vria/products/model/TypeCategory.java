package com.vria.products.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "type_categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_category")
    private Long idTypeCategory;

    @Column(name = "name_type_category", nullable = false, length = 100)
    private String nameTypeCategory;

    @Column(name = "description", length = 255)
    private String description;
}
