package com.example.phantommask.param;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaskDto {

    private String name;

    private Double price;

}
