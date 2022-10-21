package shoppingmall.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Builder
public class TokenAuthentication implements Authentication {
	
	private static final long serialVersionUID = -6335980338531508319L;
	private boolean authenticated;
	private String credentials;
	private String name;	
	private Collection<? extends GrantedAuthority> authorities;

	private String details;
	private UserDetails principal;

}
