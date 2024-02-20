package com.emh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.OffsetDateTime;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Notification
{

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer notificationId;

	@Column(nullable = false, length = 16)
	private String username;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false, columnDefinition = "longtext")
	private String notificationTitle;

	@Column(nullable = false, columnDefinition = "longtext")
	private String notificationContent;

	@Column(nullable = false)
	private OffsetDateTime timeSent;

	@OneToMany(mappedBy = "notification")
	private Set<StudentNotification> notificationStudentNotifications;

	@OneToMany(mappedBy = "notification")
	private Set<TeacherNotification> notificationTeacherNotifications;

}
