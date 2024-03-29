package com.emh.util;

import com.emh.service.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class SecurityUtils
{

	public static UserDetailsImpl getPrincipal()
	{
		return (UserDetailsImpl) (SecurityContextHolder.getContext()).getAuthentication().getPrincipal();
	}

	public static List<String> getAuthorities()
	{
		List<String> results = new ArrayList<>();
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) (SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities());
		for (GrantedAuthority authority : authorities)
			results.add(authority.getAuthority());
		return results;
	}
}