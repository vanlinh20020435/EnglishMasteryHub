package com.emh.repos;

import com.emh.entity.Classes;
import com.emh.entity.StudentNotifications;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentNotificationsRepository extends JpaRepository<StudentNotifications, Integer>
{

	StudentNotifications findFirstByClasss(Classes classes);

}
