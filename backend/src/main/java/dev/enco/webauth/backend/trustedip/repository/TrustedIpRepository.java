package dev.enco.webauth.backend.trustedip.repository;

import dev.enco.webauth.backend.trustedip.entity.TrustedIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrustedIpRepository extends JpaRepository<TrustedIp, Long> {
    List<TrustedIp> findAllByUsername(String username);
    Optional<TrustedIp> findByIdAndUsername(Long id, String username);
    boolean existsByUsernameAndIp(String username, String ip);
}
