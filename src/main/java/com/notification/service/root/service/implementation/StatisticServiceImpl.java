package com.notification.service.root.service.implementation;

import com.notification.service.root.entity.Mailing;
import com.notification.service.root.entity.Statistic;
import com.notification.service.root.exception.MailingNotFoundException;
import com.notification.service.root.repository.MailingRepository;
import com.notification.service.root.repository.StatisticRepository;
import com.notification.service.root.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatisticServiceImpl implements StatisticService {
    private final MailingRepository mailingRepository;
    private final StatisticRepository statisticRepository;

    @Autowired
    public StatisticServiceImpl(MailingRepository mailingRepository, StatisticRepository statisticRepository) {
        this.mailingRepository = mailingRepository;
        this.statisticRepository = statisticRepository;
    }

    @Override
    public List<Statistic> getStatisticForAllMailings() {
        return statisticRepository.findAll();
    }

    @Override
    public Statistic getStatisticByMailingId(Long id) {
        Optional<Mailing> mailing = mailingRepository.findById(id);
        if (mailing.isPresent()) {
            return statisticRepository.findByMailingId(id);
        } else {
            throw new MailingNotFoundException(id);
        }
    }
}
