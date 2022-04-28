package com.notification.service.root.service;

import com.notification.service.root.entity.Statistic;

import java.util.List;

public interface StatisticService {

    List<Statistic> getStatisticForAllMailings();

    Statistic getStatisticByMailingId(Long id);

}
