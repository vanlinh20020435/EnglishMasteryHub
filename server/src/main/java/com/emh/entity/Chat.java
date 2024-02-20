package com.emh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Chat
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 16)
	private String username;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false)
	private OffsetDateTime timeSent;

	@Column(nullable = false, columnDefinition = "longtext")
	private String chatContent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "classs_id", nullable = false)
	private Classs classs;

}
