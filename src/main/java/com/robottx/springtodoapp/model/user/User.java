package com.robottx.springtodoapp.model.user;

import com.robottx.springtodoapp.model.auth.RefreshToken;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Builder
@ToString
@EqualsAndHashCode
@Table(name = "users")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 254, nullable = false, unique = true)
    private String email;

    @Column(length = 30, nullable = false)
    private String username;

    @Column(length = 255, nullable = false)
    private String password;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfileImage profileImage;

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RefreshToken> refreshTokens = new HashSet<>();

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "user_id") },
                inverseJoinColumns = { @JoinColumn(name = "authority_id", referencedColumnName = "authority_id") })
    private Set<Authority> authorities = new HashSet<>();

    @CreatedDate
    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Transient
    @Builder.Default
    private boolean anonymous = false;

    @Transient
    private String accessToken;

    public static User anonymous() {
        return builder()
                .id(null)
                .anonymous(true)
                .build();
    }

    public User possessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public void setProfileImage(ProfileImage image) {
        this.profileImage = image;
    }

    public void addRefreshToken(@NotNull RefreshToken refreshToken) {
        this.refreshTokens.add(refreshToken);
        refreshToken.setUser(this);
    }

    public void removeRefreshToken(@NotNull RefreshToken refreshToken) {
        this.refreshTokens.remove(refreshToken);
        refreshToken.setUser(null);
    }

    public boolean addAuthority(Authority authority) {
        return this.authorities.add(authority);
    }

    public boolean removeAuthority(Authority authority) {
        return this.authorities.remove(authority);
    }

}
