package com.daytodayhealth.rating.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the buyer database table.
 * 
 */
@Entity
@Table(name="buyer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Buyer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Temporal(TemporalType.DATE)
	private Date dob;

	private String email;

	private String fname;

	@Column(name="is_active")
	private Byte isActive;

	@Column(name="is_deleted")
	private Byte isDeleted;

	@Column(name="is_mobile_verified")
	private Byte isMobileVerified;

	@Column(name="landline_no")
	private String landlineNo;

	private String lname;

	private String phone;

	private String salutation;

	@Column(name="user_uuid")
	private String userUuid;

}