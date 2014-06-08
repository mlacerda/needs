package com.softb.system.security.social.github;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.connect.GitHubAdapter;

public class MyGitHubConnectionFactory extends OAuth2ConnectionFactory<GitHub> {

	public MyGitHubConnectionFactory(String clientId, String clientSecret) {
		super("github", new MyGitHubServiceProvider(clientId, clientSecret), new GitHubAdapter());
	}

}
