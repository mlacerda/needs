package com.softb.system.security.social;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionSignUp;

import com.softb.system.security.model.UserAccount;
import com.softb.system.security.service.UserAccountService;

/**
 * Automaticamente registra (SignUp) o usuário após o processo de autenticação no provider (google).
 * Creata um novo usuário (UserAccount) no banco de dados, populando com os dados vindos do provider.
 *  
 */
public class ConnectionSignUpImpl implements ConnectionSignUp {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionSignUpImpl.class);
    
    private final UserAccountService userAccountService;
    
    @Inject
    public ConnectionSignUpImpl(UserAccountService userAccountService){
        this.userAccountService = userAccountService;
    }
    
    public String execute(Connection<?> connection) {
        UserAccount account = this.userAccountService.createUserAccount(builderUserAccount(connection));
        
        if (logger.isDebugEnabled()) {
        	ConnectionData data = connection.createData();
            logger.debug("Automatically create a new user account '"+account.getUserId()+"', for "+account.getDisplayName());
            logger.debug("connection data is from provider '"+data.getProviderId()+"', providerUserId is '"+data.getProviderUserId());
        }
        return account.getUserId();
    }
    
    public UserAccount builderUserAccount(Connection<?> connection) {
    	ConnectionData data = connection.createData();

    	UserAccount account = new UserAccount();
    	account.setDisplayName(data.getDisplayName());
    	account.setImageUrl(data.getImageUrl());

    	// teste acessando dados da API Git
//    	if ( "github".equals(data.getProviderId()) ) {
//            Map<String, ?> user = ((org.springframework.social.github.api.GitHub)connection.getApi()).restOperations().getForObject("https://api.github.com/user", java.util.Map.class);
//        	account.setDisplayName(data.getDisplayName());
//        	String avatarUrl = (String) user.get("avatar_url");
//        	account.setImageUrl(avatarUrl);
//
//    	}
    	
    	return account;
    }
}