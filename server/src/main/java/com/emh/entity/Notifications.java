package com.emh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
public class Notifications extends BaseEntity
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
	private Set<StudentNotifications> studentNotificationses;

	@OneToMany(mappedBy = "notification")
	private Set<TeacherNotifications> teacherNotificationses;

}
