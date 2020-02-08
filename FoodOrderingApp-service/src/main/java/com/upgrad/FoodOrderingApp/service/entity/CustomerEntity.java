package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "customer")
public class CustomerEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "UUID")
	private String uuid;

	@Column(name = "firstname")
	@NotNull
	private String firstName;

	@Column(name = "lastname")
	@NotNull
	private String lastName;

	@Column(name = "email")
	@NotNull
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "SALT")
	@NotNull
	@Size(max = 200)
	private String salt;

	@Column(name = "contact_number")
	@NotNull
	@Size(max = 200)
	private String contactNumber;
	@Override
	public boolean equals(Object obj) {
		return new EqualsBuilder().append(this, obj).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this).hashCode();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
