package com.daytodayhealth.rating.repository;

import com.daytodayhealth.rating.entity.ReviewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewTypeRepository extends JpaRepository<ReviewType, Integer> {


    ReviewType findByReviewType(String reviewedType);
}
