package com.robottx.springtodoapp.model.user;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String authority;

    public Authority(String authority) {
        this.authority = authority.toUpperCase();
    }

}
