package shoppingmall.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import shoppingmall.entity.TokenAuthentication;
import shoppingmall.service.SessionManagementService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private SessionManagementService sessionManagementService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc)
			throws ServletException, IOException 
	{
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");
		res.setHeader("Access-Control-Allow-Headers", "*");
		
		final String queryString = req.getQueryString() == null ? "" : "?" + req.getQueryString();
		
		if (req.getRequestURI().startsWith("/files") || req.getRequestURI().startsWith("//")) // Check the request
		{
	        log.info("[Request] {} {}{} - {} from {}", req.getMethod(), req.getRequestURI(), queryString, 
	        		CommUtils.getClientDevice(req), CommUtils.getClientIP(req));
		}
	        
		resolve(req, res);

		fc.doFilter(req, res);
	}

	private void resolve(HttpServletRequest req, HttpServletResponse res) throws IOException 
	{
		SecurityContext context = SecurityContextHolder.getContext(); // 當前用戶的SecurityContext
		
		if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated())
			return;
		
		String hToken = req.getHeader("Authorization"); // 取得Token值

		if (hToken != null) 
		{	
			Authentication auth = TokenAuthentication.builder().credentials(hToken).build();
			
			try 
			{
				SecurityContextHolder.getContext().setAuthentication(sessionManagementService.authenticate(auth));
			} 
			catch (AuthenticationException e) 
			{
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "token expired");
			}
		} 
		else 
		{
			SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("geofence", "null",
					Collections.singletonList(new SimpleGrantedAuthority("ANONYMOUS_ROLE"))));
		}
	}
}
