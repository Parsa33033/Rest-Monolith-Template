package com.template.monolith.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.template.monolith.auth.JwtUtils;
import com.template.monolith.auth.MainUserDetailsService;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private final String AUTHORIZATION = "Authorization";
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	MainUserDetailsService userDetailsService;
	
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorizationHeader = request.getHeader(AUTHORIZATION);
		String username = null;
		String jwt = null;
		
		if (authorizationHeader != null && (authorizationHeader.startsWith("bearer ") || authorizationHeader.startsWith("Bearer "))) {
			jwt = authorizationHeader.substring(7);
			username = jwtUtils.getUsername(jwt);
		}
		
		
		
		if (username!=null && SecurityContextHolder.getContext().getAuthentication() == null) {
			Boolean expired = jwtUtils.tokenExpired(jwt);
			if (expired) {
				redisTemplate.delete(jwt);
			}
			Boolean blacklistJwt = redisTemplate.hasKey(jwt);
			if (!blacklistJwt && !expired) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(upat);				
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
