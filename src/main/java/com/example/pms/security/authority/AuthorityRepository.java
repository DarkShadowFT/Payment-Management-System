package com.example.pms.security.authority;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByUsername(String username);
}
