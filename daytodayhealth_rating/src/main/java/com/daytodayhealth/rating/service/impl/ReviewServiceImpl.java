package com.daytodayhealth.rating.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.daytodayhealth.rating.entity.*;
import com.daytodayhealth.rating.enums.CountEnum;
import com.daytodayhealth.rating.enums.ReviewTypeEnum;
import com.daytodayhealth.rating.enums.SortByEnum;
import com.daytodayhealth.rating.repository.*;
import com.daytodayhealth.rating.response.DataResponse;
import com.daytodayhealth.rating.utils.ReviewerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.daytodayhealth.rating.constants.BeepConstants;
import com.daytodayhealth.rating.exception.ErrorCode;
import com.daytodayhealth.rating.exception.ResourceHamperedException;
import com.daytodayhealth.rating.exception.ResourceNotFoundException;
import com.daytodayhealth.rating.model.AverageReviewRatingDTO;
import com.daytodayhealth.rating.model.RatingDTO;
import com.daytodayhealth.rating.model.ReviewDTO;
import com.daytodayhealth.rating.model.ReviewRatingsDto;
import com.daytodayhealth.rating.model.ReviewerDTO;
import com.daytodayhealth.rating.model.StarRatingCountDTO;
import com.daytodayhealth.rating.response.ResponseAddEdit;
import com.daytodayhealth.rating.response.ResponseCode;
import com.daytodayhealth.rating.service.ReviewService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRatingRepository reviewRatingRepo;

	@Autowired
	private ReviewTypeRepository reviewTypeRepository;

	@Autowired
	private AverageRatingRepository averageRatingRepo;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	MessageSource messageSource;

	@Autowired
	BuyerRepository buyerRepository;

	@Override
	public DataResponse getReview(String reviewerTypeStr, Integer reviewerId, String reviewedTypeStr,
                                  Integer reviewedId, Integer page, Integer pageSize, Integer sortBy) {

		ReviewTypeEnum reviewedTypeEnum = ReviewTypeEnum.valueOf(reviewedTypeStr.toUpperCase());
		ReviewType reviewedType = reviewTypeRepository.findByReviewType(reviewedTypeEnum.getName());
		log.info("Fetching reviewed Type");

		ReviewTypeEnum reviewerTypeEnum = ReviewTypeEnum.valueOf(reviewerTypeStr.toUpperCase());
		ReviewType reviewerType = reviewTypeRepository.findByReviewType(reviewerTypeEnum.getName());
		log.info("Fetching reviewer Type");

		if (!reviewedType.getReviewType().equals(reviewedTypeEnum.getName())
				|| !reviewerType.getReviewType().equals(reviewerTypeEnum.getName())) {
			throw new ResourceHamperedException("review.type.mismatch");
		}

		AverageReviewRatingDTO reviewRatings = new AverageReviewRatingDTO();
		RatingDTO ratingDto = new RatingDTO();
		ratingDto.setStarRatingCount(new StarRatingCountDTO());
		reviewRatings.setReviewRatings(ratingDto);
		reviewRatings.setCurrentUserReview(new ReviewDTO());
		if (page == 1) {
			getAverageReviewRatingsDetail(reviewedTypeEnum, reviewedId, reviewerTypeEnum, reviewerId, reviewRatings);
		}

		getReviewRatingDetails(reviewerTypeEnum, reviewerId, reviewedTypeEnum, reviewedId, page, pageSize, sortBy,
				reviewRatings);

		return new DataResponse(ResponseCode.SUCCESS,
				messageSource.getMessage("review.success", null, LocaleContextHolder.getLocale()), reviewRatings);
	}

	private void getAverageReviewRatingsDetail(ReviewTypeEnum reviewedTypeEnum, Integer reviewedId,
			ReviewTypeEnum reviewerTypeEnum, Integer reviewerId, AverageReviewRatingDTO reviewRatingsDto) {

		AverageRating averageRating = averageRatingRepo.findByReviewedTypeIdAndReviewedId(reviewedTypeEnum.getId(),
				reviewedId);
		log.info("Fetching average rating.");
		if (averageRating != null) {
			setAverageRating(averageRating, reviewRatingsDto);
		}

		if (reviewerId > 0) {
			ReviewerDTO reviewerDto = ReviewerUtility.reviewerExists(reviewerId, reviewerTypeEnum.getId());
			ReviewRating currentuserReviewRating = reviewRatingRepo
					.findByIsDeletedAndReviewerIdAndReviewedIdAndReviewerTypeIdAndReviewedTypeId((byte) 0, reviewerId,
							reviewedId, reviewerTypeEnum.getId(), reviewedTypeEnum.getId());
			log.info("Fetching logged in user review.");
			if (currentuserReviewRating != null) {
				reviewRatingsDto.setIsRatedByCurrentUser(true);
				setCurrentUserReview(currentuserReviewRating, reviewerDto, reviewRatingsDto);
			} else {
				reviewRatingsDto.setIsRatedByCurrentUser(false);
				reviewRatingsDto.setCurrentUserReview(new ReviewDTO());
			}
		}
	}

	private int getReviewRatingDetails(ReviewTypeEnum reviewerTypeEnum, Integer reviewerId,
			ReviewTypeEnum reviewedTypeEnum, Integer reviewedId, Integer page, Integer pageSize, Integer sortBy,
			AverageReviewRatingDTO reviewRatings) {
		List<ReviewRating> otherUserReviewRatingList = new ArrayList<>();
		List<ReviewDTO> otherUserReviewRatingDTOList = new ArrayList<>();

		int totalRecords = setOtherUserReviewList(reviewerTypeEnum, reviewerId, reviewedTypeEnum, reviewedId,
				otherUserReviewRatingList, sortBy, page, pageSize);
		if (!otherUserReviewRatingList.isEmpty()) {
			convertOtherUserRating(reviewerTypeEnum, otherUserReviewRatingList, otherUserReviewRatingDTOList);
		}
		reviewRatings.setOtherReviews(otherUserReviewRatingDTOList);
		reviewRatings.setOtherReviewsCount(totalRecords);
		return totalRecords;
	}

	private int setOtherUserReviewList(ReviewTypeEnum reviewerTypeEnum, Integer reviewerId,
			ReviewTypeEnum reviewedTypeEnum, Integer reviewedId, List<ReviewRating> otherUserReviewRatingList,
			Integer sortBy, Integer page, Integer pageSize) {
		int totalRecords = 0;
		if (sortBy == SortByEnum.HIGHESTRATING.getValue()) {
			otherUserReviewRatingList.addAll(reviewRatingRepo
					.findByIsDeletedAndReviewedIdAndReviewerTypeIdAndReviewedTypeIdAndReviewerIdNotOrderByRatingDesc(
							(byte) 0, reviewedId, reviewerTypeEnum.getId(), reviewedTypeEnum.getId(), reviewerId,
							PageRequest.of(page == null ? 0 : page - 1, pageSize == null ? 10 : pageSize)));
			totalRecords = reviewRatingRepo
					.countByIsDeletedAndReviewedIdAndReviewerTypeIdAndReviewedTypeIdAndReviewerIdNotOrderByRatingDesc(
							(byte) 0, reviewedId, reviewerTypeEnum.getId(), reviewedTypeEnum.getId(), reviewerId);
		} else if (sortBy == SortByEnum.LOWESTRATING.getValue()) {
			otherUserReviewRatingList.addAll(reviewRatingRepo
					.findByIsDeletedAndReviewedIdAndReviewerTypeIdAndReviewedTypeIdAndReviewerIdNotOrderByRatingAsc(
							(byte) 0, reviewedId, reviewerTypeEnum.getId(), reviewedTypeEnum.getId(), reviewerId,
							PageRequest.of(page == null ? 0 : page - 1, pageSize == null ? 10 : pageSize)));
			totalRecords = reviewRatingRepo
					.countByIsDeletedAndReviewedIdAndReviewerTypeIdAndReviewedTypeIdAndReviewerIdNotOrderByRatingAsc(
							(byte) 0, reviewedId, reviewerTypeEnum.getId(), reviewedTypeEnum.getId(), reviewerId);
		} else {
			otherUserReviewRatingList.addAll(reviewRatingRepo
					.findByIsDeletedAndReviewedIdAndReviewerTypeIdAndReviewedTypeIdAndReviewerIdNotOrderByCreatedDateDesc(
							(byte) 0, reviewedId, reviewerTypeEnum.getId(), reviewedTypeEnum.getId(), reviewerId,
							PageRequest.of(page == null ? 0 : page - 1, pageSize == null ? 10 : pageSize)));
			totalRecords = reviewRatingRepo
					.countByIsDeletedAndReviewedIdAndReviewerTypeIdAndReviewedTypeIdAndReviewerIdNotOrderByCreatedDateDesc(
							(byte) 0, reviewedId, reviewerTypeEnum.getId(), reviewedTypeEnum.getId(), reviewerId);
		}
		log.info("Fetching all user reviews of the store.");
		return totalRecords;
	}

	private void setCurrentUserReview(ReviewRating currentuserReviewRating, ReviewerDTO reviewerDto,
			AverageReviewRatingDTO reviewRatingsDto) {
		log.info("Setting current user review.");
		ReviewDTO currentUserReviewDTO = new ReviewDTO();
		currentUserReviewDTO.setDateTime(
				currentuserReviewRating.getUpdatedDate() != null ? currentuserReviewRating.getUpdatedDate().getTime()
						: currentuserReviewRating.getCreatedDate().getTime());
		currentUserReviewDTO.setReviewId(currentuserReviewRating.getId());
		currentUserReviewDTO.setReview(currentuserReviewRating.getReview());
		currentUserReviewDTO.setRating(currentuserReviewRating.getRating());
		currentUserReviewDTO.setSenderName(reviewerDto.getFname() + " " + reviewerDto.getLname());
		reviewRatingsDto.setCurrentUserReview(currentUserReviewDTO);
	}

	private AverageReviewRatingDTO setAverageRating(AverageRating averageRating,
			AverageReviewRatingDTO reviewRatingsDto) {
		log.info("Populating response dto for average rating.");
		RatingDTO ratingDto = new RatingDTO();
		if (averageRating.getAvgRating() != null) {
			ratingDto.setOverallRating(averageRating.getAvgRating());
		}
		if (averageRating.getReviewCount() != null) {
			ratingDto.setOverallReviewCount(averageRating.getReviewCount());
		}
		ratingDto.setStarRatingCount(getDynamicStarReviewCount(averageRating));
		reviewRatingsDto.setReviewRatings(ratingDto);
		return reviewRatingsDto;
	}

	private StarRatingCountDTO getDynamicStarReviewCount(AverageRating averageRating) {
		log.info("Populating response dto for percentage stars.");
		StarRatingCountDTO starRatingCountDTO = new StarRatingCountDTO();
		if (averageRating.getPercent1star() != null) {
			starRatingCountDTO.setPercent1star(averageRating.getPercent1star());
		}
		if (averageRating.getPercent2star() != null) {
			starRatingCountDTO.setPercent2star(averageRating.getPercent2star());
		}
		if (averageRating.getPercent3star() != null) {
			starRatingCountDTO.setPercent3star(averageRating.getPercent3star());
		}
		if (averageRating.getPercent4star() != null) {
			starRatingCountDTO.setPercent4star(averageRating.getPercent4star());
		}
		if (averageRating.getPercent5star() != null) {
			starRatingCountDTO.setPercent5star(averageRating.getPercent5star());
		}
		return starRatingCountDTO;
	}

	private void convertOtherUserRating(ReviewTypeEnum reviewerTypeEnum, List<ReviewRating> otherUserReviewRatingList,
			List<ReviewDTO> otherUserReviewRatingDTOList) {
		log.info("Get Other User Review Details.");
		otherUserReviewRatingList.stream()
				.map((Function<? super ReviewRating, ? extends Auditable>) otherUserReviewRating -> {
					ReviewDTO otherUserReviewRatingDTO = new ReviewDTO();
					otherUserReviewRatingDTO.setDateTime(otherUserReviewRating.getUpdatedDate() != null
							? otherUserReviewRating.getUpdatedDate().getTime()
							: otherUserReviewRating.getCreatedDate().getTime());
					otherUserReviewRatingDTO.setReviewId(otherUserReviewRating.getId());
					otherUserReviewRatingDTO.setReview(otherUserReviewRating.getReview());
					otherUserReviewRatingDTO.setRating(otherUserReviewRating.getRating());
					ReviewerDTO reviewerDto = ReviewerUtility.reviewerExists(otherUserReviewRating.getReviewerId(),
							reviewerTypeEnum.getId());
					otherUserReviewRatingDTO.setSenderName(reviewerDto.getFname() + " " + reviewerDto.getLname());
					otherUserReviewRatingDTOList.add(otherUserReviewRatingDTO);
					return otherUserReviewRating;
				}).collect(Collectors.toList());
	}

	@Override
	public ResponseAddEdit addEditReview(Integer reviewerId, String reviewerTypeStr, Integer reviewedId,
			String reviewedTypeStr, ReviewRatingsDto reviewRatingsDto) {
		log.info("Add Edit Review");
		log.info("Checking if rating is given > 0 as default value of rating is 0.");
		if(!(buyerRepository.existsById(reviewerId)) || !(productRepository.existsById(reviewedId))){
			throw new ResourceNotFoundException(ErrorCode.ReviewError.WRONG_ID_ERROR);
		}
		if (reviewRatingsDto.getRating() == 0 || reviewRatingsDto.getRating() > 5) {
			throw new ResourceNotFoundException(ErrorCode.ReviewError.RECORD_NOT_FOUND);
		}

		ReviewTypeEnum reviewedTypeEnum = ReviewTypeEnum.valueOf(reviewedTypeStr.toUpperCase());
		ReviewType reviewedType = reviewTypeRepository.findByReviewType(reviewedTypeEnum.getName());
		log.info("Fetching reviewed Type");

		ReviewTypeEnum reviewerTypeEnum = ReviewTypeEnum.valueOf(reviewerTypeStr.toUpperCase());
		ReviewType reviewerType = reviewTypeRepository.findByReviewType(reviewerTypeEnum.getName());
		log.info("Fetching reviewer Type");

		if (!reviewedType.getReviewType().equals(reviewedTypeEnum.getName())
				|| !reviewerType.getReviewType().equals(reviewerTypeEnum.getName())) {
			throw new ResourceHamperedException("review.type.mismatch");
		}

		ReviewRating reviewRating = null;
		Optional<ReviewRating> review = reviewRatingRepo
				.findByReviewedTypeIdAndReviewedIdAndReviewerTypeIdAndReviewerId(reviewedTypeEnum.getId(), reviewedId,
						reviewerType.getId(), reviewerId);
		if (review.isPresent()) {
			reviewRating = review.get();
			reviewRating.setRating(reviewRatingsDto.getRating());
			reviewRating.setReview(reviewRatingsDto.getReview());
		} else {
			reviewRating = new ReviewRating();
			reviewRating.setReviewedId(reviewedId);
			reviewRating.setReviewerId(reviewerId);
			reviewRating.setRating(reviewRatingsDto.getRating());
			reviewRating.setReview(reviewRatingsDto.getReview());
			reviewRating.setReviewerType(reviewerType);
			reviewRating.setReviewedType(reviewedType);
		}
		reviewRatingRepo.save(reviewRating);
		log.info("Saving into review rating table.");
		saveAvgRatingReview(reviewedType, reviewedId, reviewerType);
		return new ResponseAddEdit(ResponseCode.SUCCESS,
				messageSource.getMessage("review.success", null, LocaleContextHolder.getLocale()));
	}

	private void saveAvgRatingReview(ReviewType reviewedType, Integer reviewedId, ReviewType reviewerType) {

		List<ReviewRating> reviewRatingList = reviewRatingRepo.findByReviewedTypeIdAndReviewedIdAndReviewerTypeId(
				reviewedType.getId(), reviewedId, reviewerType.getId());
		if (reviewRatingList.isEmpty()) {
			throw new ResourceNotFoundException(ErrorCode.ReviewError.EMPTY_LIST);
		}
		AverageRating averageRating = averageRatingRepo.findByReviewedTypeIdAndReviewedId(reviewedType.getId(),
				reviewedId);

		Integer count1star = BeepConstants.ZERO;
		Integer count2star = BeepConstants.ZERO;
		Integer count3star = BeepConstants.ZERO;
		Integer count4star = BeepConstants.ZERO;
		Integer count5star = BeepConstants.ZERO;
		Integer reviewCount = BeepConstants.ZERO;

		for (ReviewRating reviewRatings : reviewRatingList) {
			if (CountEnum.ONE.getValue() == reviewRatings.getRating()) {
				count1star++;
			} else if (CountEnum.TWO.getValue() == reviewRatings.getRating()) {
				count2star++;
			} else if (CountEnum.THREE.getValue() == reviewRatings.getRating()) {
				count3star++;
			} else if (CountEnum.FOUR.getValue() == reviewRatings.getRating()) {
				count4star++;
			} else {
				count5star++;
			}
			reviewCount = (count1star + count2star + count3star + count4star + count5star);
		}

		Double avgRating = (((double) (count1star) + (double) (count2star * 2) + (double) (count3star * 3)
				+ (double) (count4star * 4) + (double) (count5star * 5)) / reviewCount);

		Double percent1str = (Double.valueOf(count1star) / Double.valueOf(reviewCount)) * 100;
		Double percent2str = (Double.valueOf(count2star) / Double.valueOf(reviewCount)) * 100;
		Double percent3str = (Double.valueOf(count3star) / Double.valueOf(reviewCount)) * 100;
		Double percent4str = (Double.valueOf(count4star) / Double.valueOf(reviewCount)) * 100;
		Double percent5str = (Double.valueOf(count5star) / Double.valueOf(reviewCount)) * 100;

		log.info("Computed average rating, review rating, percent stars etc.");
		if (averageRating != null) {
			averageRating.setReviewCount(reviewCount);
			averageRating.setCount1start(count1star);
			averageRating.setCount2start(count2star);
			averageRating.setCount3start(count3star);
			averageRating.setCount4start(count4star);
			averageRating.setCount5start(count5star);
			averageRating.setAvgRating(avgRating);
			averageRating.setPercent1star(percent1str);
			averageRating.setPercent2star(percent2str);
			averageRating.setPercent3star(percent3str);
			averageRating.setPercent4star(percent4str);
			averageRating.setPercent5star(percent5str);
		} else {
			averageRating = new AverageRating();
			averageRating.setReviewedType(reviewedType);
			averageRating.setReviewedId(reviewedId);
			averageRating.setReviewCount(reviewCount);
			averageRating.setCount1start(count1star);
			averageRating.setCount2start(count2star);
			averageRating.setCount3start(count3star);
			averageRating.setCount4start(count4star);
			averageRating.setCount5start(count5star);
			averageRating.setAvgRating(avgRating);
			averageRating.setPercent1star(percent1str);
			averageRating.setPercent2star(percent2str);
			averageRating.setPercent3star(percent3str);
			averageRating.setPercent4star(percent4str);
			averageRating.setPercent5star(percent5str);

		}
		averageRatingRepo.save(averageRating);
		log.info("Saving into average rating table.");
	}

}
