package com.daytodayhealth.rating.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AverageReviewRatingDTO {
	
	private RatingDTO reviewRatings;

	private Boolean isRatedByCurrentUser = null;

	private ReviewDTO currentUserReview;
	
	private int otherReviewsCount;
	
	private List<ReviewDTO> otherReviews;
	
}
