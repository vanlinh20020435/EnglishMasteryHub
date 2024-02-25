package com.emh.security.jwt;

import com.emh.service.CustomUserDetailsService;
import com.emh.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class AuthTokenFilter extends OncePerRequestFilter
{
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	private String parseJwt(HttpServletRequest request)
	{
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer "))
		{
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException
	{
		try
		{
			final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (isEmpty(header) || !header.startsWith("Bearer "))
			{
				chain.doFilter(request, response);
				return;
			}
			String jwt = parseJwt(request);
			if (!request.getRequestURI().contains("signin") &&
					!request.getRequestURI().contains("refreshtoken"))
			{
				if (jwt == null)
					throw new Exception("JWT is null!!");
			}
			if (jwtUtils.validateJwtToken(jwt))
			{
				String username = jwtUtils.getUserNameFromJwtToken(jwt);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		catch (Exception e)
		{
			logger.error("Cannot set user authentication: {}", e.getMessage());
		}

		chain.doFilter(request, response);
	}
}