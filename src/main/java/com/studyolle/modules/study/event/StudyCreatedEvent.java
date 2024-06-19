package com.studyolle.modules.study.event;

import com.studyolle.modules.study.Study;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StudyCreatedEvent {

    private final Study study;

    public StudyCreatedEvent(final Study study) {
        this.study = study;
    }
}
