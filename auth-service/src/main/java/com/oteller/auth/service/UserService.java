package com.oteller.auth.service;

import com.oteller.auth.model.User;
import com.oteller.common.service.GenericService;

/**
 * Kullanıcı servis arayüzü.
 * GenericService'in sağladığı CRUD operasyonlarına ek olarak
 * kullanıcı adına göre arama işlevi sunar.
 */
public interface UserService extends GenericService<User, Long> {
    /**
     * Kullanıcı adına göre kullanıcı bulur.
     *
     * @param username kullanıcı adı
     * @return bulunan kullanıcı
     * @throws RuntimeException kullanıcı bulunamazsa
     */
    User findByUsername(String username);
} 