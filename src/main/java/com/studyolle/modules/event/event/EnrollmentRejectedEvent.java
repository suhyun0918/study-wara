package com.studyolle.modules.event.event;

import com.studyolle.modules.event.Enrollment;

public class EnrollmentRejectedEvent extends EnrollmentEvent {
    public EnrollmentRejectedEvent(final Enrollment enrollment) {
        super(enrollment, "모임 참가 신청이 거절 되었습니다.");
    }
}
