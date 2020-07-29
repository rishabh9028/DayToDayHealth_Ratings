package com.daytodayhealth.rating.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="review_rating")
public class ReviewRating extends Auditable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@Column(name="is_deleted")
	private Byte isDeleted = 0;

	private Byte rating;

	private String review;

	@Column(name="reviewed_id")
	private Integer reviewedId;

	@Column(name="reviewer_id")
	private Integer reviewerId;

	@ManyToOne
	@JoinColumn(name="reviewer_type_id")
	private ReviewType reviewerType;

	@ManyToOne
	@JoinColumn(name="reviewed_type_id")
	private ReviewType reviewedType;

}
