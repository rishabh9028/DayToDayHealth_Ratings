package com.daytodayhealth.rating.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRatingsDto {
	
    private byte rating;

    private String review;
}
