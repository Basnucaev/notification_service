package com.notification.service.root.repository;

import com.notification.service.root.entity.Mailing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MailingRepository extends JpaRepository<Mailing, Long> {

    @Query("from Mailing m where m.sentStatus = 'UNSENT' and m.startMailingDate < :current_date")
    List<Mailing> findAllBySentStatusIsUNSENTAndTimeHasCome(@Param("current_date") LocalDateTime localDateTime);
}
