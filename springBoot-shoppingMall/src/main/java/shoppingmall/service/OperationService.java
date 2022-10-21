package shoppingmall.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import shoppingmall.entity.AdminUser;
import shoppingmall.utils.BusinessLayerResponse;
import shoppingmall.utils.CommUtils;
import shoppingmall.utils.EnDecoderUtil;
import shoppingmall.vo.BackstageLoginVo;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class OperationService {
    
    @Autowired
    private AdminUserService adminUserService;
    
    @Autowired
    private SessionManagementService sessionManagementService;
    
    // 後台登入
    public BusinessLayerResponse<String> getToken(HttpServletRequest request, BackstageLoginVo backstageLoginVo)
    {
        if (!StringUtils.hasText(backstageLoginVo.getName()))
            return BusinessLayerResponse.error("請輸入帳號");
        
        if (!StringUtils.hasText(backstageLoginVo.getPassword()))
            return BusinessLayerResponse.error("請輸入密碼");

        if (backstageLoginVo.getWeb() == null)
            return BusinessLayerResponse.error("請輸入類別");

        log.info("OperationService ==> getToken ... 後台登入 : [" + backstageLoginVo + "]");
        
        String name = StringUtils.trimWhitespace(backstageLoginVo.getName());
        String password = StringUtils.trimWhitespace(backstageLoginVo.getPassword());
        
        String ip = CommUtils.getClientIP(request);

        String md5Password = EnDecoderUtil.md5Encrypt(password); // 密碼加密
        
        // 管理員登入
        if (backstageLoginVo.getWeb().equals(0))
        {
            AdminUser adminUser = adminUserService.findByUserName(name);
            
            if (adminUser == null)
                BusinessLayerResponse.error("查無使用者");
            
            if (!adminUser.getPassword().equals(md5Password))
                return BusinessLayerResponse.error("密碼錯誤");

            String token = sessionManagementService.removeLoggedUser(request, "admin-" + adminUser.getUsername(), 0);

            if (token != null)
                return BusinessLayerResponse.ok(token);
            
            else
            {
                adminUser.setLastLoginIP(ip);
                adminUser.setLastLoginTime(System.currentTimeMillis());
                adminUserService.save(adminUser);

                log.info("OperationService ==> 登入 ...... >> 總後台 : [" + name + "]");

                return BusinessLayerResponse.ok(sessionManagementService.getTokenAdmin(adminUser));
            }
        }
        
        return BusinessLayerResponse.error("登入失敗!");
    }
}
