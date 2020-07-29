package com.daytodayhealth.rating.enums;

public enum ReviewTypeEnum {


	BUYER(1, "Buyer"),
	PRODUCT(2, "Product");

	private final String name;
	private final int id;

	ReviewTypeEnum(final int id, final String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

}
