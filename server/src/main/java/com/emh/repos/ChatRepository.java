package com.emh.repos;

import com.emh.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRepository extends JpaRepository<Chat, Integer> {
}
