package com.notification.service.root.repository;

import com.notification.service.root.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findMessageByClientIdAndMailingId(Long client_id, Long mailing_id);
}
