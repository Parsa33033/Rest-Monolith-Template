package com.template.monolith.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseId {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
}
