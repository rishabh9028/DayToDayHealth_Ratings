package com.daytodayhealth.rating.controller;

import javax.validation.constraints.NotNull;

import com.daytodayhealth.rating.constants.UrlConstants;
import com.daytodayhealth.rating.model.ReviewRatingsDto;
import com.daytodayhealth.rating.response.ResponseAddEdit;
import com.daytodayhealth.rating.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@ApiResponses({ @ApiResponse(code = 200, message = "Successful."),
		@ApiResponse(code = 204, message = "Successful, no content."),
		@ApiResponse(code = 400, message = "Bad request. Validation failure."),
		@ApiResponse(code = 201, message = "Entity created"), @ApiResponse(code = 404, message = "Entity not found"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 500, message = "Internal Server Error") })
@Api(value = "Review", tags = { "Review" })
public class ReviewController extends BaseController {

	@Autowired
    ReviewService reviewService;

	@ApiOperation("Get Review of Stores")
	@GetMapping(value = UrlConstants.REVIEW_URL)
	public ResponseEntity<?> getReview(@PathVariable("reviewerType") String reviewerType ,
			@PathVariable("reviewerId") Integer reviewerId, @PathVariable("reviewedType") String reviewedType,
			@PathVariable("reviewedId") Integer reviewedId,
			@RequestParam(name = "pageSize", required = false, defaultValue = "20") final Integer pageSize,
			@NotNull @RequestParam(name = "page") final Integer page,
			@NotNull @RequestParam(name = "sortBy") final Integer sortBy,
			@RequestHeader(value = "userToken", required = false) final String userToken) {
		return ResponseEntity.ok(
				reviewService.getReview(reviewerType, reviewerId, reviewedType, reviewedId, page, pageSize, sortBy));
	}

	@ApiOperation("Add/Edit Review Rating")
	@PostMapping(value = UrlConstants.REVIEW_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseAddEdit> addEditReview(@PathVariable(name = "reviewerId") final Integer reviewerId,
                                                         @PathVariable(name = "reviewerType") final String reviewerType,
                                                         @PathVariable(name = "reviewedId") final Integer reviewedId,
                                                         @PathVariable(name = "reviewedType") final String reviewedType,
                                                         @RequestBody final ReviewRatingsDto reviewRatingsDto) {
		return ResponseEntity
				.ok(reviewService.addEditReview(reviewerId, reviewerType, reviewedId, reviewedType, reviewRatingsDto));
	}

}
