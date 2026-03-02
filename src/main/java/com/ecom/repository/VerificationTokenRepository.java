package com.ecom.repository;

import com.ecom.entity.User;
import com.ecom.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    void deleteByUser(User user);

    List<VerificationToken> findByUser_IdOrderByIdDesc(Long id);
}
