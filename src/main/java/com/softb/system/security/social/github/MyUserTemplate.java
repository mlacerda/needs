package com.softb.system.security.social.github;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.social.github.api.impl.UserTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * Sobrecarga no método getUserProfile para retornar a URL avatar correta. Propriedade profileImageUrl
 */
public class MyUserTemplate extends UserTemplate {

	private RestTemplate restTemplate;

	public MyUserTemplate(RestTemplate restTemplate, boolean isAuthorizedForUser) {
		super(restTemplate, isAuthorizedForUser);
		this.restTemplate = restTemplate;
	}
	
	@Override
	public GitHubUserProfile getUserProfile() {
		@SuppressWarnings("unchecked")
		Map<String, ?> user = restTemplate.getForObject(buildUri("user"), Map.class);		
		Long gitHubId = Long.valueOf(String.valueOf(user.get("id")));
		String username = String.valueOf(user.get("login"));
		String name = String.valueOf(user.get("name"));
		String location = user.get("location") != null ? String.valueOf(user.get("location")) : null;
		String company = user.get("company") != null ? String.valueOf(user.get("company")) : null;
		String blog = user.get("blog") != null ? String.valueOf(user.get("blog")) : null;
		String email = user.get("email") != null ? String.valueOf(user.get("email")) : null;
		Date createdDate = toDate(String.valueOf(user.get("created_at")), dateFormat);
		String profileImageUrl = user.get("avatar_url") != null ? String.valueOf(user.get("avatar_url")) : null;
		return new GitHubUserProfile(gitHubId, username, name, location, company, blog, email, profileImageUrl, createdDate);
	}
	
	private Date toDate(String dateString, DateFormat dateFormat) {
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}
	
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z", Locale.ENGLISH);

}
