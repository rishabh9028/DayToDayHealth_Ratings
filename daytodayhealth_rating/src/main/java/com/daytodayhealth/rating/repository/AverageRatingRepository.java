package com.daytodayhealth.rating.repository;

import com.daytodayhealth.rating.entity.AverageRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AverageRatingRepository extends JpaRepository<AverageRating, Long>{

	AverageRating findByReviewedTypeIdAndReviewedId(int reviewedTypeId, int reviewedId);
	
}
