package shoppingmall.vo;

import lombok.Data;

import shoppingmall.entity.AdminUser;
import shoppingmall.entity.MemberUser;

@Data
public class SessionVO {
   
    private String token;
    
    private AdminUser adminUser; // 管理者
    
    private MemberUser memberUser; // 會員
}
