package com.emh.security;

import com.emh.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler
{

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException
	{
		String targetUrl = determineTargetUrl(authentication, request);
		if (targetUrl.contains("?action=login"))
		{
			targetUrl = targetUrl.replace("?action=login", "");
		}
		if (response.isCommitted())
		{
			return;
		}
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	public RedirectStrategy getRedirectStrategy()
	{
		return redirectStrategy;
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy)
	{
		this.redirectStrategy = redirectStrategy;
	}

	private String determineTargetUrl(Authentication authentication, HttpServletRequest request)
	{
		String url = "";
		List<String> roles = SecurityUtils.getAuthorities();
		if (isAdmin(roles))
		{
			url = "/quan-tri/trang-chu";
		}
		else if (isUser(roles))
		{
			url = request.getHeader("referer");
		}
		return url;
	}

	private boolean isAdmin(List<String> roles)
	{
		if (roles.contains("ROLE_ADMIN"))
		{
			return true;
		}
		return false;
	}

	private boolean isUser(List<String> roles)
	{
		if (roles.contains("ROLE_USER"))
		{
			return true;
		}
		return false;
	}
}