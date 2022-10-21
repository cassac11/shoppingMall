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

import javax.persistence.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AdminUser implements UserDetails {

    @Id
    @Column(nullable = false, length = 10)
    private String adminName;

    @Column(nullable = false)
    private String password;

    @LastModifiedDate
    private Date modifyTime;

    @CreatedDate
    private Date createDate;

    @Column(nullable = false)
    private Boolean enable;

    @Column(nullable = false, length = 30)
    @Convert(converter = StringListConverter.class)
    private List<String> roles;

    @Column(columnDefinition = "json")
    private String info;
    
    private Long lastLoginTime;
    
    private String lastLoginIP;

    @SuppressWarnings("unchecked")
    public Map<String, String> getInfo() 
    {
        if (info == null) return new HashMap<>();
        
        try 
        {
            return new ObjectMapper().readValue(info, HashMap.class);
        } 
        catch (IOException e) 
        {
            return new HashMap<>();
        }
    }

    public void setInfo(Map<String, String> info) 
    {
        if (info == null) return;
        
        try 
        {
            this.info = new ObjectMapper().writeValueAsString(info);
        } 
        catch (JsonProcessingException e) 
        {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
    
    @Override
    public String getUsername() {
        return this.adminName;
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
        return false;
    }
}
