package com.daytodayhealth.rating.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
	
	private Double overallRating = 0.00;

	private Integer overallReviewCount = 0;

	private StarRatingCountDTO starRatingCount;

}
