package shoppingmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling  // 啟動 @Schedule 定時任務
@EnableJpaAuditing // 自動CreateDate、LastModifiedDate 賦值
@SpringBootApplication
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class SpringBootShoppingMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootShoppingMallApplication.class, args);
    }

}
