package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.backend.entity.account.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByRole(String role);
}
