package com.studyolle.modules.study;

import com.studyolle.modules.account.Account;
import com.studyolle.modules.study.event.StudyCreatedEvent;
import com.studyolle.modules.study.event.StudyUpdateEvent;
import com.studyolle.modules.study.form.StudyDescriptionForm;
import com.studyolle.modules.study.form.StudyForm;
import com.studyolle.modules.tag.Tag;
import com.studyolle.modules.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public Study createNewStudy(Study study, Account account) {
        Study newStudy = studyRepository.save(study);
        newStudy.addManager(account);
         return newStudy;
    }

    public Study getStudyToUpdate(Account account, String path) {
        Study study = getStudy(path);
        if (!study.isManagedBy(account)) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
        return study;
    }

    public Study getStudy(final String path) {
        Study study = studyRepository.findByPath(path);
        if (study == null) {
            throw new IllegalArgumentException(path + "에 해당하는 스터디가 없습니다.");
        }
        return study;
    }

    public void updateStudyDescription(Study study, StudyDescriptionForm studyDescriptionForm) {
        modelMapper.map(studyDescriptionForm, study);
        eventPublisher.publishEvent(new StudyUpdateEvent(study, "스터디 소개를 수정했습니다."));
    }

    public void updateStudyImage(Study study, String image) {
        study.setImage(image);
    }

    public void enableStudyBanner(final Study study) {
        study.setUseBanner(true);
    }

    public void disableStudyBanner(final Study study) {
        study.setUseBanner(false);
    }

    public void addTag(Study study, Tag tag) {
        study.getTags().add(tag);
    }

    public void removeTag(Study study, Tag tag) {
        study.getTags().remove(tag);
    }

    public void addZone(Study study, Zone zone) {
        study.getZones().add(zone);
    }

    public void removeZone(Study study, Zone zone) {
        study.getZones().remove(zone);
    }

    public Study getStudyToUpdateTag(Account account, String path) {
        Study study = studyRepository.findStudyWithTagsByPath(path);
        checkIfExistingStudy(path, study);
        checkIfManager(account, study);
        return study;
    }

    public Study getStudyToUpdateZone(Account account, String path) {
        Study study = studyRepository.findStudyWithZonesByPath(path);
        checkIfExistingStudy(path, study);
        checkIfManager(account, study);
        return study;
    }

    private void checkIfManager(final Account account, final Study study) {
        if (!study.isManagedBy(account)) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
    }

    private void checkIfExistingStudy(final String path, final Study study) {
        if (study == null) {
            throw new IllegalArgumentException(path + "에 해당하는 스터디가 없습니다.");
        }
    }

    public void publish(final Study study) {
        study.publish();
        eventPublisher.publishEvent(new StudyCreatedEvent(study));
    }

    public void close(final Study study) {
        study.close();
        eventPublisher.publishEvent(new StudyUpdateEvent(study, "스터디를 종료했습니다."));
    }

    public Study getStudyToUpdateStatus(final Account account, final String path) {
        Study study = studyRepository.findStudyWithManagersByPath(path);
        checkIfExistingStudy(path, study);
        checkIfManager(account, study);
        return study;
    }

    public void startRecruit(final Study study) {
        study.startRecruit();
        eventPublisher.publishEvent(new StudyUpdateEvent(study, "팀원 모집을 시작합니다."));
    }

    public void stopRecruit(final Study study) {
        study.stopRecruit();
        eventPublisher.publishEvent(new StudyUpdateEvent(study, "팀원 모집을 중단했습니다."));
    }

    public void updateStudyPath(final Study study, final String newPath) {
        study.setPath(newPath);
    }

    public void updateStudyTitle(final Study study, final String newTitle) {
        study.setTitle(newTitle);
    }

    public boolean isValidTitle(final String newTitle) {
        return newTitle.length() <= 50;
    }


    public boolean isValidPath(final String newPath) {
        if (!newPath.matches(StudyForm.VALID_PATH_PATTERN)) {
            return false;
        }
        return !studyRepository.existsByPath(newPath);
    }

    public void remove(final Study study) {
        if (study.isRemovable()) {
            studyRepository.delete(study);
        } else {
            throw new IllegalArgumentException("스터디를 삭제할 수 없습니다.");
        }
    }

    public void addMember(final Study study, final Account account) {
        study.addMember(account);
    }

    public void removeMember(final Study study, final Account account) {
        study.removeMember(account);
    }

    public Study getStudyToEnroll(final String path) {
        Study study = studyRepository.findStudyOnlyByPath(path);
        checkIfExistingStudy(path, study);
        return study;
    }
}
