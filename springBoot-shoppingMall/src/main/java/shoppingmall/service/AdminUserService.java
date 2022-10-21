package shoppingmall.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import shoppingmall.entity.AdminUser;
import shoppingmall.repository.AdminUserRepository;
import shoppingmall.utils.BusinessLayerResponse;
import shoppingmall.utils.CommUtils;
import shoppingmall.utils.EnDecoderUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Service
public class AdminUserService {
    
    @Autowired
    private AdminUserRepository adminUserRepository;
    
    public AdminUser findByUserName(String name)
    {
        Optional<AdminUser> byId = adminUserRepository.findById(name);

        return byId.orElse(null);
    }
    
    public AdminUser save(AdminUser adminUser)
    {
        return adminUserRepository.save(adminUser);
    }

    // 新增管理員
    public BusinessLayerResponse<String> createAdminUser(HttpServletRequest request, String adminUser, String password)
    {
        if (!StringUtils.hasText(adminUser))
        {
            return BusinessLayerResponse.error("用戶名不可為空!");
        }

        if (!StringUtils.hasText(password))
        {
            return BusinessLayerResponse.error("密碼不可為空!");
        }

        if (!adminUser.matches("[a-zA-Z0-9]+"))
        {
            return BusinessLayerResponse.error("用戶名為数字或字母组合！");
        }

        String adminName = StringUtils.trimWhitespace(adminUser);
        String adminPassword = StringUtils.trimWhitespace(password);
        String clientIP = CommUtils.getClientIP(request);

        AdminUser user = findByUserName(adminName);

        if (user != null)
            return BusinessLayerResponse.error("用戶已注冊！");
        
        // 註冊管理員
        user = new AdminUser();
        user.setAdminName(adminName);
        user.setEnable(true);
        user.setInfo(new HashMap<>());
        user.setPassword(EnDecoderUtil.md5Encrypt(adminPassword)); // 加密
        user.setRoles(Collections.singletonList("ROLE_ADMIN")); // 管理員權限
        
        save(user);

        log.info("AdminUserService ==> createAdminUser ... 新增管理員 : [" + adminName + "]");
        
        return BusinessLayerResponse.ok("註冊成功");
    }
}
