package shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shoppingmall.entity.Mail;

@Repository
public interface MailRepository extends JpaRepository<Mail, String> {
}
