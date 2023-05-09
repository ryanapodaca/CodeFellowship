package com.auth.auth.repos;

import com.auth.auth.models.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {
    public SiteUser findByUsername(String username);
}
