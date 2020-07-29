package com.daytodayhealth.rating.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="review_type_mst")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	@Column(name="review_type")
	private String reviewType;

	//bi-directional many-to-one association to AvgRating
	@OneToMany(mappedBy="reviewedType")
	private List<AverageRating> avgRatings;

	//bi-directional many-to-one association to ReviewRating
	@OneToMany(mappedBy="reviewedType")
	private List<ReviewRating> reviewRatings;
	
}
