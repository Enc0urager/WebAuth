package dev.enco.webauth.backend.minecraft.entity;

import dev.enco.webauth.backend.minecraft.enums.JoinRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "join_requests")
public class JoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String ip;

    @Column
    private String country;

    @Column
    private String city;

    @Column(nullable = false)
    private Instant requestedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JoinRequestStatus status;
}
