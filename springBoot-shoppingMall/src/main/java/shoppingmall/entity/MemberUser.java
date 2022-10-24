package shoppingmall.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import shoppingmall.converter.StringListConverter;
import shoppingmall.key.MemberKey;

import javax.persistence.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MemberUser implements UserDetails {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private MemberKey key;

    @Column(nullable = false)
    private String password; // 密碼(加密)

    private String nickName; // 暱稱
    private Integer sex; // 性別: 0 男 、 1 女
    private Date birthday;
    
    private String email;
    private String mobile;
    private String areaCode;
    private String profile_img_url; // 大頭照圖檔地址
    
    private Boolean enable; // 是否啟用
    private Boolean isOnline; //是否在線
    
    @LastModifiedDate
    private Date modifyTime;

    @CreatedDate
    private Date createDate;

    @Column(nullable = false,length = 30)
    @Convert(converter = StringListConverter.class)
    private List<String> roles;

    @Column(columnDefinition = "json")
    private String info;
    
    private String lastLoginIP;
    private Long lastLoginTime;

    private Integer lockCount; // 登入次數
    private Long tempLockTime; // 暫時鎖帳時間
    private Boolean lockType;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return key.getMember();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getInfo() {
        if (info == null) {
            return new HashMap<>();
        }
        try 
        {
            return new ObjectMapper().readValue(info, HashMap.class);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public void setInfo(Map<String, String> info) {
        if (info == null) {
            return;
        }
        try {
            this.info = new ObjectMapper().writeValueAsString(info);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
