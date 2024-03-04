package com.emh.repos;

import com.emh.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationsRepository extends JpaRepository<Notifications, Integer>
{
}
