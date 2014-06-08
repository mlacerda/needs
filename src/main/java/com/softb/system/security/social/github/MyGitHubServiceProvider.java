package com.softb.system.security.social.github;

import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.connect.GitHubServiceProvider;

public class MyGitHubServiceProvider extends GitHubServiceProvider {

	public MyGitHubServiceProvider(String clientId, String clientSecret) {
		super(clientId, clientSecret);
	}

	@Override
	public GitHub getApi(String accessToken) {
		return new MyGitHubTemplate(accessToken);
	} 
	
}
