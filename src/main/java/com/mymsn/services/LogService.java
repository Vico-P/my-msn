package com.mymsn.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mymsn.entities.Log;
import com.mymsn.repository.LogRepository;

@Service
// In order to save in DB even if an error is thrown by the other services or
// rest controller because new transaction won't be cancel by thrown error
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class LogService {
    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public Log saveLog(Log toSave) {
        return this.logRepository.save(toSave);
    }
}
