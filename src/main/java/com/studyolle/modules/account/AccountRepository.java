package com.studyolle.modules.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long>, QuerydslPredicateExecutor<Account> {
    Account findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Account findByNickname(String emailOrNickname);
}
