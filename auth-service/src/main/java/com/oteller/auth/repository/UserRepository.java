package com.oteller.auth.repository;

import com.oteller.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Kullanıcı veri erişim arayüzü.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Kullanıcı adına göre kullanıcı bulur.
     *
     * @param username kullanıcı adı
     * @return bulunan kullanıcı, yoksa boş Optional
     */
    Optional<User> findByUsername(String username);
} 