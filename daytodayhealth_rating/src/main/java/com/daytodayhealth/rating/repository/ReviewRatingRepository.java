package com.daytodayhealth.rating.repository;

import java.util.List;
import java.util.Optional;

import com.daytodayhealth.rating.entity.ReviewRating;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRatingRepository extends JpaRepository<ReviewRating, Long> {

	ReviewRating findByIsDeletedAndReviewerIdAndReviewedIdAndReviewerTypeIdAndReviewedTypeId(byte isDeleted,
			Integer reviewerId, Integer reviewedId, Integer reviewerTypeId, Integer reviewedTypeId);

	List<ReviewRating> findByIsDeletedAndReviewedIdAndReviewerTypeIdAndReviewedTypeIdAndReviewerIdNotOrderByRatingDesc(
			byte isDeleted, Integer reviewdId, Integer reviewerTypeId, Integer reviewedTypeId, Integer reviewerId,
			Pageable limit);

	int countByIsDeletedAndReviewedIdAndReviewerTypeIdAndReviewedTypeIdAndReviewerIdNotOrderByRatingDesc(byte isDeleted,
			Integer reviewdId, Integer reviewerTypeId, Integer reviewedTypeId, Integer reviewerId);

	List<ReviewRating> findByIsDeletedAndReviewedIdAndReviewerTypeIdAndReviewedTypeIdAndReviewerIdNotOrderByRatingAsc(
			byte isDeleted, Integer reviewdId, Integer reviewerTypeId, Integer reviewedTypeId, Integer reviewerId,
			Pageable limit);

	int countByIsDeletedAndReviewedIdAndReviewerTypeIdAndReviewedTypeIdAndReviewerIdNotOrderByRatingAsc(byte isDeleted,
			Integer reviewdId, Integer reviewerTypeId, Integer reviewedTypeId, Integer reviewerId);

	List<ReviewRating> findByIsDeletedAndReviewedIdAndReviewerTypeIdAndReviewedTypeIdAndReviewerIdNotOrderByCreatedDateDesc(
			byte isDeleted, Integer reviewdId, Integer reviewerTypeId, Integer reviewedTypeId, Integer reviewerId,
			Pageable limit);

	int countByIsDeletedAndReviewedIdAndReviewerTypeIdAndReviewedTypeIdAndReviewerIdNotOrderByCreatedDateDesc(
			byte isDeleted, Integer reviewdId, Integer reviewerTypeId, Integer reviewedTypeId, Integer reviewerId);

	Optional<ReviewRating> findByReviewedTypeIdAndReviewedIdAndReviewerTypeIdAndReviewerId(Integer reviewedTypeId,
			Integer reviewedId, Integer reviewerTypeId, Integer revierweId);

	List<ReviewRating> findByReviewedTypeIdAndReviewedIdAndReviewerTypeId(Integer reviewedTypeId, Integer reviewedId,
			Integer reviewerTypeId);
}
