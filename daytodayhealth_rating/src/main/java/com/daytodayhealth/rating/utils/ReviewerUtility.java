package com.daytodayhealth.rating.utils;

import com.daytodayhealth.rating.enums.ReviewTypeEnum;
import com.daytodayhealth.rating.model.ReviewerDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.daytodayhealth.rating.entity.Buyer;
import com.daytodayhealth.rating.exception.ResourceNotFoundException;
import com.daytodayhealth.rating.repository.BuyerRepository;

@Component
public class ReviewerUtility {
	
	static BuyerRepository buyerRepo;
	static ModelMapper modelMapper;
	
	@Autowired
    public ReviewerUtility(BuyerRepository buyerRepo, ModelMapper modelMapper) {
		ReviewerUtility.buyerRepo = buyerRepo;
		ReviewerUtility.modelMapper = modelMapper;
	}

	public static ReviewerDTO reviewerExists(Integer reviewerId, Integer reviewerTypeId) {
		ReviewerDTO reviewerDTO = new ReviewerDTO();
		if(reviewerTypeId == ReviewTypeEnum.BUYER.getId()) {
			Buyer buyer = buyerRepo.findById(reviewerId)
					.orElseThrow(() -> new ResourceNotFoundException("reviewer.invalid"));
			reviewerDTO = modelMapper.map(buyer, ReviewerDTO.class);
		}
		return reviewerDTO;
	}

}
