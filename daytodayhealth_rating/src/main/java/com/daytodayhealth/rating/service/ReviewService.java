package com.daytodayhealth.rating.service;

import com.daytodayhealth.rating.model.ReviewRatingsDto;
import com.daytodayhealth.rating.response.DataResponse;
import com.daytodayhealth.rating.response.ResponseAddEdit;

public interface ReviewService {

	DataResponse getReview(String reviewerType, Integer reviewerId, String reviewedType, Integer reviewedId,
                           Integer page, Integer pageSize, Integer sortBy);

	ResponseAddEdit addEditReview(Integer reviewerId, String reviewerType, Integer reviewedId, String reviewedType,
			ReviewRatingsDto reviewRatingsDto);
}
