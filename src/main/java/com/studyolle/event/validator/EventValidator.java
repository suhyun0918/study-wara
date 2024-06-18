package com.studyolle.event.validator;

import com.studyolle.event.form.EventForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class EventValidator implements Validator {
    @Override
    public boolean supports(final Class<?> clazz) {
        return EventForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        EventForm eventForm = (EventForm) target;
        if (!isValidEndEnrollmentDateTime(eventForm)) {
            errors.rejectValue("endEnrollmentDateTime", "wrong.datetime", "모임 접수 종료 일시를 정확히 입력하세요.");
        }
        if (!isValidEndDateTime(eventForm)) {
            errors.rejectValue("endDateTime", "wrong.datetime", "모임 종료 일시를 정확히 입력하세요.");
        }
        if (!isValidStartDateTime(eventForm)) {
            errors.rejectValue("startDateTime", "wrong.datetime", "모임 시작 일시를 정확히 입력하세요.");
        }
    }

    private boolean isValidStartDateTime(final EventForm eventForm) {
        return !eventForm.getStartDateTime().isBefore(eventForm.getEndEnrollmentDateTime());
    }

    private boolean isValidEndEnrollmentDateTime(final EventForm eventForm) {
        return !eventForm.getEndEnrollmentDateTime().isBefore(LocalDateTime.now());
    }

    private boolean isValidEndDateTime(final EventForm eventForm) {
        LocalDateTime endDateTime = eventForm.getEndDateTime();
        return !endDateTime.isBefore(eventForm.getStartDateTime()) ||
                !endDateTime.isBefore(eventForm.getEndEnrollmentDateTime());
    }
}
