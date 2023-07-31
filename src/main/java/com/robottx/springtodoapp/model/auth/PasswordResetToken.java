package com.robottx.springtodoapp.model.auth;

import com.robottx.springtodoapp.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID token;

    private LocalDateTime expiryTime;

    @CreatedDate
    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

}
