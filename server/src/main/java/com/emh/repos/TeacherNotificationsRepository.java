package com.emh.repos;

import com.emh.entity.Teacher;
import com.emh.entity.TeacherNotifications;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeacherNotificationsRepository extends JpaRepository<TeacherNotifications, Integer>
{

	TeacherNotifications findFirstByTeacher(Teacher teacher);

}
