package com.emh.repos;

import com.emh.entity.TeacherNotification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeacherNotificationRepository extends JpaRepository<TeacherNotification, Integer> {
}
