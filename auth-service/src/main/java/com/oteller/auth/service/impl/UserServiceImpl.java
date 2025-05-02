package com.oteller.auth.service.impl;

import com.oteller.auth.model.User;
import com.oteller.auth.repository.UserRepository;
import com.oteller.auth.service.UserService;
import com.oteller.common.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Kullanıcı servis implementasyonu.
 * GenericServiceImpl'den türer ve UserService arayüzünü uygular.
 */
@Service
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {

    private final UserRepository userRepository;

    /**
     * Constructor injection ile bağımlılıkları alır.
     * 
     * @param userRepository kullanıcı repository'si
     */
    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    /**
     * Kullanıcı adına göre kullanıcı bulur, yoksa hata fırlatır.
     *
     * @param username kullanıcı adı
     * @return bulunan kullanıcı
     * @throws RuntimeException kullanıcı bulunamazsa
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
} 