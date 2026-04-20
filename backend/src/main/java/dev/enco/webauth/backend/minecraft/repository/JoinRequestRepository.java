package dev.enco.webauth.backend.minecraft.repository;

import dev.enco.webauth.backend.minecraft.entity.JoinRequest;
import dev.enco.webauth.backend.minecraft.enums.JoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {
    List<JoinRequest> findAllByUsername(String username);
    List<JoinRequest> findAllByUsernameAndStatus(String username, JoinRequestStatus status);
}
