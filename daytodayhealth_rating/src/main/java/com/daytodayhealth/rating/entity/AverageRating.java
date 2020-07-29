package com.daytodayhealth.rating.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "avg_rating")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AverageRating implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@Column(name="avg_rating")
	private Double avgRating;

	@Column(name="count_1start")
	private Integer count1start;

	@Column(name="count_2start")
	private Integer count2start;

	@Column(name="count_3start")
	private Integer count3start;

	@Column(name="count_4start")
	private Integer count4start;

	@Column(name="count_5start")
	private Integer count5start;

	@Column(name="percent_1star")
	private Double percent1star;

	@Column(name="percent_2star")
	private Double percent2star;

	@Column(name="percent_3star")
	private Double percent3star;

	@Column(name="percent_4star")
	private Double percent4star;

	@Column(name="percent_5star")
	private Double percent5star;

	@Column(name="review_count")
	private Integer reviewCount;

	@Column(name="reviewed_id")
	private Integer reviewedId;

	//bi-directional many-to-one association to ReviewTypeMst
	@ManyToOne
	@JoinColumn(name="reviewed_type_id")
	private ReviewType reviewedType;
	
}
