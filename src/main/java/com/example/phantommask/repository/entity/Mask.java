package com.example.phantommask.repository.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "masks")
@Setter
@Getter
@NoArgsConstructor
public class Mask {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    @Schema(description = "商品名稱", example = "3M N95")
    private String name;

    @Column(nullable = false)
    @Schema(description = "口罩顏色", example = "白色")
    private String color;

    @Column(nullable = false)
    @Schema(description = "包裝數量", example = "50")
    private Integer packSize;

    @Column(nullable = false)
    @Schema(description = "價格", example = "199.99")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    @JsonIgnore
    private Pharmacies pharmacy;

}
