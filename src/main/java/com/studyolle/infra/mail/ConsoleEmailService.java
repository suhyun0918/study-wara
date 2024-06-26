package com.studyolle.infra.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Slf4j
@Component
public class ConsoleEmailService implements EmailService {

    @Override
    public void sendEmail(final EmailMessage emailMessage) {
        log.info("sent email: {}", emailMessage.getMessage());
    }
}
