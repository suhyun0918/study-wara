package com.studyolle.modules.notification;

import com.studyolle.modules.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Long countByAccountAndChecked(Account account, boolean checked);

    List<Notification> findByAccountAndCheckedOrderByCreatedDateTimeDesc(Account account, boolean b);

    @Transactional
    void deleteByAccountAndChecked(Account account, boolean checked);
}
