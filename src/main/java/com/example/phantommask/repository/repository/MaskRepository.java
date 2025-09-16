package com.example.phantommask.repository.repository;

import com.example.phantommask.repository.entity.Mask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaskRepository extends JpaRepository<Mask, Long> {
    List<Mask> findByPackSizeLessThanAndPriceBetween(Integer size, Double minPrice, Double maxPrice);


    List<Mask> findByPackSizeGreaterThanAndPriceBetween(Integer size, Double minPrice, Double maxPrice);

    List<Mask> findByNameContainingIgnoreCase(String name);
}
