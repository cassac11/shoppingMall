package shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shoppingmall.entity.AdminUser;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, String> {
}
