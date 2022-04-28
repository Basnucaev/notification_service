package com.notification.service.root.service.implementation;

import com.notification.service.root.entity.Mailing;
import com.notification.service.root.exception.EntityNotSavedException;
import com.notification.service.root.exception.EntityNotUpdatedException;
import com.notification.service.root.exception.MailingNotFoundException;
import com.notification.service.root.repository.MailingRepository;
import com.notification.service.root.service.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
            return mailing.get();
        } else {
            throw new MailingNotFoundException(id);
        }
    }

    @Override
    public List<Mailing> getAllMailings() {
        return mailingRepository.findAll();
    }

    @Override
    public void saveMailing(Mailing mailing) {
        Long id = mailing.getId();
        if (id == null) {
            mailingRepository.save(mailing);
        } else {
            throw new EntityNotSavedException("Mailing");
        }
    }

    @Override
    public void updateMailing(Mailing mailing) {
        Long id = mailing.getId();
        if (id != null && id != 0) {
            mailingRepository.save(mailing);
        } else {
            throw new EntityNotUpdatedException("Mailing");
        }
    }

    @Override
    public void deleteMailingById(Long id) {
        Mailing mailing = getMailingById(id);
        mailingRepository.deleteById(id);
    }
}
