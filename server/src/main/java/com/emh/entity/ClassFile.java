package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class ClassFile extends BaseEntity
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, updatable = false)
	private String type;

	@Column(nullable = false)
	private String url;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String documentName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "classs_id", nullable = false)
	private Classes classs;

}
