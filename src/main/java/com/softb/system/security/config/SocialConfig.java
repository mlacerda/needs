package com.softb.system.security.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SocialAuthenticationServiceLocator;

import com.softb.system.security.repository.UserSocialConnectionRepository;
import com.softb.system.security.service.UserAccountService;
import com.softb.system.security.social.ConnectControllerImpl;
import com.softb.system.security.social.ConnectionSignUpImpl;
import com.softb.system.security.social.UsersConnectionRepositoryImpl;
import com.softb.system.security.social.github.MyGitHubConnectionFactory;

/**
 * Configuração para Spring Social.
 * 
 */
@Configuration
@EnableSocial
//@ComponentScan(basePackages={"org.springframework.social.connect.web"})
public class SocialConfig implements SocialConfigurer {


	private static final String SOCIAL_GOOGLE_CLIENT_ID = "application.social.google.clientId";
    private static final String SOCIAL_GOOGLE_CLIENT_SECRET = "application.social.google.clientSecret";	

	private static final String SOCIAL_GITHUB_CLIENT_ID = "application.social.github.clientIddd";
    private static final String SOCIAL_GITHUB_CLIENT_SECRET = "application.social.github.clientSecret";	

	@Inject
    private UserSocialConnectionRepository userSocialConnectionRepository;
    
    @Inject
    private UserAccountService userAccountService;
    
    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        cfConfig.addConnectionFactory(new GoogleConnectionFactory(env.getProperty(SOCIAL_GOOGLE_CLIENT_ID), env.getProperty(SOCIAL_GOOGLE_CLIENT_SECRET)));
//        cfConfig.addConnectionFactory(new MyGitHubConnectionFactory("507678491e1920512104", "e9600534084fe630fa0d632dca63a89b84187612"));
        cfConfig.addConnectionFactory(new MyGitHubConnectionFactory(env.getProperty(SOCIAL_GITHUB_CLIENT_ID), env.getProperty(SOCIAL_GITHUB_CLIENT_SECRET)));
    }

	@Bean
	public UserIdSource userIdSource() {
		return new AuthenticationNameUserIdSource();
	}
    
    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        UsersConnectionRepositoryImpl repository = new UsersConnectionRepositoryImpl(
                userSocialConnectionRepository, (SocialAuthenticationServiceLocator)connectionFactoryLocator, Encryptors.noOpText());
        repository.setConnectionSignUp(new ConnectionSignUpImpl(userAccountService));
        return repository;
    }
    
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public Google google(ConnectionRepository repository) {
        Connection<Google> connection = repository.findPrimaryConnection(Google.class);
        return connection != null ? connection.getApi() : null;
    }
    
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public GitHub github(ConnectionRepository repository) {
        Connection<GitHub> connection = repository.findPrimaryConnection(GitHub.class);
        return connection != null ? connection.getApi() : null;
    }
    
    @Bean
    public ConnectController buildConnectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository repository) {
    	ConnectController connectController = new ConnectControllerImpl(connectionFactoryLocator, repository);
    	return connectController;
    }
    
}
