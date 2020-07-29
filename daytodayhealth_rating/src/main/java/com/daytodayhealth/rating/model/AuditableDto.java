package com.daytodayhealth.rating.model;

/**
 * @author :Ketan K. Chandegara
 * @purpose :Entity Auditable Utility
 */
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AuditableDto implements Serializable {

	private static final long serialVersionUID = 4013959554770897896L;
	private Date createdDate;

}
