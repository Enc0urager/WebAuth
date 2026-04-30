package dev.enco.webauth.backend.oauth.entity;

import dev.enco.webauth.backend.oauth.model.OAuthProvider;
import dev.enco.webauth.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "oauth_users")
public class OAuthUser {
    @Id @UuidGenerator
    private String id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username",  nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuthProvider provider;

    @Column(nullable = false)
    private String providerUserId;

    @Column(length = 254)
    private String email;
}
