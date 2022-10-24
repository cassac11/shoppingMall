package shoppingmall.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import shoppingmall.entity.AdminUser;
import shoppingmall.entity.MemberUser;
import shoppingmall.entity.TokenAuthentication;
import shoppingmall.utils.CommUtils;
import shoppingmall.vo.SessionVO;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
public class SessionManagementService implements AuthenticationProvider {
	
	private final Map<String, SessionVO> sessionMap = new ConcurrentHashMap<>();
	
	private final Map<String, Set<String>> userIndexMap = new ConcurrentHashMap<>();
	
	// 檢查過期時間
	@Scheduled(fixedRate = 30000)
	public void scheduleFixedDelayTask() 
	{
		for (Entry<String, SessionVO> e : sessionMap.entrySet()) 
		{
			String token = e.getKey();
			
			AdminUser admin = e.getValue().getAdminUser(); // 管理者
			MemberUser memberUser = e.getValue().getMemberUser(); // 會員
			
			String timeStr = null;
			
			if (admin != null)
				timeStr = admin.getInfo().get("refresh");
			
			else if (memberUser != null)
				timeStr = memberUser.getInfo().get("refresh");
			
			if (timeStr == null) 
			{
				if (admin != null) 
				{
					e.getValue().getAdminUser().getInfo().put("refresh", Long.valueOf(System.currentTimeMillis()).toString());
					e.getValue().getAdminUser().getInfo().put("reTime", "sys");
				}
				
				else if (memberUser != null)
				{
					e.getValue().getMemberUser().getInfo().put("refresh", Long.valueOf(System.currentTimeMillis()).toString());
					e.getValue().getMemberUser().getInfo().put("reTime", "sys");
				}
				
				sessionMap.put(token, e.getValue());
				return;
			}

			long resSec = System.currentTimeMillis() - Long.parseLong(timeStr);
			
			if (resSec > (3600L * 1000)) // 過期
			{
				if (admin != null)
				{
					log.info("expired:" + admin.getUsername() + ",resSec:" + resSec);
					removeToken(token, 0);
				}
				else if (memberUser != null)
				{
					log.info("expired:" + memberUser.getUsername() + ",resSec:" + resSec);
					removeToken(token, 1);
				}
			}
		}
	}
	
	// 移除Token
	public String removeLoggedUser(HttpServletRequest request, String userId, Integer web) 
	{
		if (userIndexMap.get(userId) == null)
			return null;
		
		userIndexMap.get(userId).forEach(index -> 
		{
			SessionVO session = sessionMap.get(index);
			
			if (session == null) 
				return;
		
			if (web == 0) 
			{
				if (("admin-" +  session.getAdminUser().getUsername()).equals(userId)) 
				{
					String userAgent = request.getHeader("User-Agent");
					log.info("removeLoggedUser:" + userId + ",loginToken:" + session.getToken() + ",userAgent:" + userAgent);
					log.info("FORWARDED:" + CommUtils.getClientIP(request));
				
					removeToken(index, 0);
				}
			}
			else if (web == 1)
			{
				if (("member_" +  session.getMemberUser().getKey().getMember()).equals(userId))
				{
					String userAgent = request.getHeader("User-Agent");
					log.info("removeLoggedUser:" + userId + ",loginToken:" + session.getToken() + ",userAgent:" + userAgent);
					log.info("FORWARDED:" + CommUtils.getClientIP(request));
				
					removeToken(index, 1);
				}
			}
			
		});
		
		return null;
	}

	// 取得管理員Token
	public String getTokenAdmin(AdminUser u) 
	{
		String key = UUID.randomUUID().toString();
		refreshTokenTimeAdmin(key, u);
		return key;
	}

	public String getTokenMember(MemberUser u)
	{
		String key = UUID.randomUUID().toString();
		refreshTokenTimeMember(key, u);
		return key;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException 
	{
		if (authentication.getCredentials() == null) 
		{
			throw new AuthenticationCredentialsNotFoundException("No credentials found in context");
		}
		
		String token = authentication.getCredentials().toString();
		return loginViaToken(token);
	}

	private TokenAuthentication loginViaToken(String token) 
	{
		SessionVO session = sessionMap.get(token);
		
		if (session == null)
			return null;
		
		AdminUser adminUser = session.getAdminUser();
		
		if (adminUser != null) 
		{
			refreshTokenTimeAdmin(token, adminUser);
			return TokenAuthentication.builder().name(adminUser.getUsername()).principal(adminUser).credentials(adminUser.getPassword())
					.authorities(adminUser.	getAuthorities()).authenticated(true).build();
		} 
	
		else
			throw new DisabledException("User is disabled");
	}

	// 重製Token時間
	private void refreshTokenTimeAdmin(String key, AdminUser u) 
	{
		long current = System.currentTimeMillis();
		
		u.getInfo().put("refresh", Long.toString(current));

		SessionVO session = sessionMap.get(key);

		if (session == null)
			session = new SessionVO();
		
		session.setAdminUser(u);
		session.setToken(key);
		sessionMap.put(key, session);

		Set<String> userIndex = userIndexMap.get("admin-" + u.getUsername());
		
		if (userIndex == null) 
		{
			userIndex = new HashSet<>();
			userIndex.add(key);
		} 
		else
			userIndex.add(key);
		
		userIndexMap.put("admin-" + u.getUsername(), userIndex);
	}

	private void refreshTokenTimeMember(String key, MemberUser u)
	{
		long current = System.currentTimeMillis();
		u.getInfo().put("refresh", Long.toString(current));

		SessionVO session = sessionMap.get(key);

		if (session == null)
			session = new SessionVO();

		session.setMemberUser(u);
		session.setToken(key);
		sessionMap.put(key, session);

		Set<String> userIndex = userIndexMap.get("member_" + u.getKey().getMember());
		
		if (userIndex == null)
		{
			userIndex = new HashSet<>();
			userIndex.add(key);
		}
		else
			userIndex.add(key);
		
		userIndexMap.put("member_" + u.getKey().getMember(), userIndex);
	}
	
	@Override
	public boolean supports(Class<?> authentication) 
	{
		return false;
	}

	// 移除Token
	public void removeToken(String token, Integer web) 
	{
		SessionVO session = sessionMap.get(token);
		
		if (session == null) 
			return;
		
		String userId = null;
	
		if (web == 0)
			userId = "admin-" + session.getAdminUser().getUsername();
		
		else if (web == 1)
			userId = "member_" + session.getMemberUser().getKey().getMember();
		
		Set<String> index = userIndexMap.get(userId);
		index.remove(token);
		
		if (index.size() == 0)
			userIndexMap.remove(userId);
		
		else
			userIndexMap.put(userId, index);
		
		sessionMap.remove(token);
	}
}
