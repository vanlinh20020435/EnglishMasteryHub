package com.emh.repos;

import com.emh.entity.StudentNotification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentNotificationRepository extends JpaRepository<StudentNotification, Integer> {
}
