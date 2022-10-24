package shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shoppingmall.entity.SysConfig;

@Repository
public interface SysConfigRepository extends JpaRepository<SysConfig, String> {
}
