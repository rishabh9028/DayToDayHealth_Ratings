package com.daytodayhealth.rating.repository;

import com.daytodayhealth.rating.entity.Product;
import com.daytodayhealth.rating.entity.ReviewRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
