package com.example.phantommask.param;

import com.example.phantommask.repository.entity.Mask;
import com.example.phantommask.repository.entity.OpeningHour;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class PharmaciesDto {


    private String name;

    private Double cashBalance;

    private String openingHours;

    private List<MaskDto> masks;
}
