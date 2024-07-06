package org.ecommerce.onlineshop.repository;
import org.ecommerce.onlineshop.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
