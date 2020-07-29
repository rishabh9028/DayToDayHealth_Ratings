package com.daytodayhealth.rating.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO{

	Long reviewId;
	
	String review = "";

	byte rating = 0;
	
	String senderName = "";
	
	Long dateTime;

}
