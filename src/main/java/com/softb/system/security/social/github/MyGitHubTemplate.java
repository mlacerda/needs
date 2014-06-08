package com.softb.system.security.social.github;

import org.springframework.social.github.api.UserOperations;
import org.springframework.social.github.api.impl.GitHubTemplate;

public class MyGitHubTemplate extends GitHubTemplate {

	private UserOperations userOperations;
	
	public MyGitHubTemplate(String accessToken) {
		super(accessToken);
		initMyUserOperation();
	}
	
	public UserOperations userOperations() { 
		return userOperations; 
	}
	
	private void initMyUserOperation() {
		this.userOperations = new MyUserTemplate(getRestTemplate(), isAuthorized());
	}

}
