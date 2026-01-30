package com.db.bankingapi.filters;


import com.db.bankingapi.configurations.JWTManager;
import com.db.bankingapi.exceptions.JwtTokenMissingException;
import com.db.bankingapi.models.User;
import com.db.bankingapi.services.UserAuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JWTManager jwtManager;

	@Autowired
	private UserAuthService userAuthService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");
		String path = request.getRequestURI();
		if (path.startsWith("/forms/v1.0") || path.startsWith("/images/favicon.ico") || path.startsWith("/images") || path.equals("/favicon.ico")) {
			filterChain.doFilter(request, response);
			return;
		}else {
			if (header == null || !header.startsWith("Bearer")) {
				throw new JwtTokenMissingException("No JWT token found in the request headers");
			}
		}


		String token = header.substring(7);

		// Optional - verification
		jwtManager.validateToken(token);

		User user = jwtManager.getUser(token);

		UserDetails userDetails = userAuthService.loadUserByUsername(user.getUserName());

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());

		usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}

		filterChain.doFilter(request, response);
	}

}
