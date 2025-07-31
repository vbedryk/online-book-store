package com.example.bookstore.repository.role;

import com.example.bookstore.model.Role;
import com.example.bookstore.model.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.name = :name")
    Optional<Role> findRoleByName(RoleName roleName);
}
