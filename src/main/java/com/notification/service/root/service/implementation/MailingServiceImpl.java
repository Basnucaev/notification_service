package com.notification.service.root.service.implementation;

import com.notification.service.root.entity.Mailing;
import com.notification.service.root.entity.enumeration.SentStatus;
import com.notification.service.root.exception.EntityNotSavedException;
import com.notification.service.root.exception.EntityNotUpdatedException;
import com.notification.service.root.exception.MailingNotFoundException;
import com.notification.service.root.repository.MailingRepository;
import com.notification.service.root.service.MailingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MailingServiceImpl implements MailingService {
    private final MailingRepository mailingRepository;

    @Autowired
    public MailingServiceImpl(MailingRepository mailingRepository) {
        this.mailingRepository = mailingRepository;
    }

    @Override
    public Mailing getMailingById(Long id) {
        Optional<Mailing> mailing = mailingRepository.findById(id);
        if (mailing.isPresent()) {
            log.info("method \"getMailingById\" | returned mailing with id= {}", id);
            return mailing.get();
        } else {
            log.info("method \"getMailingById\" | mailing with id= {} not found", id);
            throw new MailingNotFoundException(id);
        }
    }

    @Override
    public List<Mailing> getAllMailings() {
        log.info("method \"getAllMailings\" | returned list of mailings");
        return mailingRepository.findAll();
    }

    @Override
    public void saveMailing(Mailing mailing) {
        if (mailing.getId() == null) {
            mailing.setSentStatus(SentStatus.UNSENT);
            mailingRepository.save(mailing);
            log.info("method \"saveMailing\" | mailing saved, assigned id= {}", mailing.getId());
        } else {
            log.info("method \"saveMailing\" | mailing not saved, mailing already have id");
            throw new EntityNotSavedException("Mailing");
        }
    }

    @Override
    public void updateMailing(Mailing mailing) {
        if (mailing.getId() != null && mailing.getId() != 0) {
            mailingRepository.save(mailing);
            log.info("method \"updateMailing\" | mailing with id= {} updated", mailing.getId());
        } else {
            log.info("method \"updateMailing\" | mailing not updated, mailing has no id");
            throw new EntityNotUpdatedException("Mailing");
        }
    }

    @Override
    public void deleteMailingById(Long id) {
        Mailing mailing = getMailingById(id);
        log.info("method \"deleteMailingById\" | mailing with id= {} deleted", id);
        mailingRepository.deleteById(id);
    }
}
