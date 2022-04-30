package com.notification.service.root.service.implementation;

import com.notification.service.root.entity.Mailing;
import com.notification.service.root.exception.EntityNotSavedException;
import com.notification.service.root.exception.EntityNotUpdatedException;
import com.notification.service.root.exception.MailingNotFoundException;
import com.notification.service.root.repository.MailingRepository;
import com.notification.service.root.service.MailingService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MailingServiceImpl implements MailingService {
    private final MailingRepository mailingRepository;
    private final Logger logger = LoggerFactory.getLogger(MailingServiceImpl.class);

    @Autowired
    public MailingServiceImpl(MailingRepository mailingRepository) {
        this.mailingRepository = mailingRepository;
    }

    @Override
    public Mailing getMailingById(Long id) {
        Optional<Mailing> mailing = mailingRepository.findById(id);
        if (mailing.isPresent()) {
            logger.info("method \"getMailingById\" | returned mailing with id= {}", id);
            return mailing.get();
        } else {
            logger.info("method \"getMailingById\" | mailing with id= {} not found", id);
            throw new MailingNotFoundException(id);
        }
    }

    @Override
    public List<Mailing> getAllMailings() {
        logger.info("method \"getAllMailings\" | returned list of mailings");
        return mailingRepository.findAll();
    }

    @Override
    public void saveMailing(Mailing mailing) {
        if (mailing.getId() == null) {
            mailingRepository.save(mailing);
            logger.info("method \"saveMailing\" | mailing saved, assigned id= {}", mailing.getId());
        } else {
            logger.info("method \"saveMailing\" | mailing not saved, mailing already have id");
            throw new EntityNotSavedException("Mailing");
        }
    }

    @Override
    public void updateMailing(Mailing mailing) {
        if (mailing.getId() != null && mailing.getId() != 0) {
            mailingRepository.save(mailing);
            logger.info("method \"updateMailing\" | mailing with id= {} updated", mailing.getId());
        } else {
            logger.info("method \"updateMailing\" | mailing not updated, mailing has no id");
            throw new EntityNotUpdatedException("Mailing");
        }
    }

    @Override
    public void deleteMailingById(Long id) {
        Mailing mailing = getMailingById(id);
        logger.info("method \"deleteMailingById\" | mailing with id= {} deleted", id);
        mailingRepository.deleteById(id);
    }
}
