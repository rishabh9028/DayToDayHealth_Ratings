package com.daytodayhealth.rating.enums;

public enum SortByEnum {

	MOSTRECENT(1), 
	HIGHESTRATING(2),
	LOWESTRATING(3);

	private final int value;

	SortByEnum(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
