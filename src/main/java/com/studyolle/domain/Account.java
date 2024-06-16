package com.studyolle.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@Getter @Setter @EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime emailCheckTokenGeneratedAt;

    private LocalDateTime joinedAt;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    @Lob // text 타입에 매핑이 됨
    @Basic(fetch = FetchType.EAGER) // 기본 Lazy.. 이 profileImage 값들은 바뀔 수 있기 때문에 명시적으로.. user 로딩 시에 많이 쓰여서 eager
    private String profileImage;

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb = true;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb = true;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb = true;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    public void generateEmailCheckToken() {
        emailCheckToken = UUID.randomUUID().toString();
        emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public void completeSignUp() {
        emailVerified = true;
        joinedAt = LocalDateTime.now();
    }

    public boolean isValidToken(final String token) {
        return emailCheckToken.equals(token);
    }

    public boolean canSendConfirmEmail() {
        return emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
    }

    public boolean isManagerOf(Study study) {
        return study.getManagers().contains(this);
    }
}
