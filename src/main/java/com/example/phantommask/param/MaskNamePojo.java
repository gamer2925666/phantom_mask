package com.example.phantommask.param;

import jakarta.persistence.Column;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaskNamePojo {
    private String name;

    private String color;

    private Integer packSize;
}
