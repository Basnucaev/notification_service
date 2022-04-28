package com.notification.service.root.repository;

import com.notification.service.root.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    Statistic findByMailingId(Long mailing_id);
}
