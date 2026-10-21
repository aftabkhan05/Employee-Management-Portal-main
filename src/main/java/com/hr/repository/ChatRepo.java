package com.hr.repository;

import com.hr.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<Chat, Integer> {

    @Query("SELECT c FROM Chat c WHERE " +
            "(c.senderId = :user1 AND c.receiverId = :user2) OR " +
            "(c.senderId = :user2 AND c.receiverId = :user1) " +
            "ORDER BY c.sentAt ASC")
    List<Chat> findChatBetween(
            @Param("user1") int user1,
            @Param("user2") int user2);

    @Query("SELECT DISTINCT c.senderId FROM Chat c " +
            "WHERE c.receiverId = :adminId AND c.senderRole = 'USER'")
    List<Integer> findEmployeesChatted(@Param("adminId") int adminId);

    @Query("SELECT COUNT(c) FROM Chat c " +
            "WHERE c.receiverId = :receiverId AND c.senderRole = 'USER'")
    Long countUnread(@Param("receiverId") int receiverId);
}