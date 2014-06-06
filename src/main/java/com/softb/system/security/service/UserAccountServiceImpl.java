package com.softb.system.security.service;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import com.softb.system.errorhandler.exception.BusinessException;
import com.softb.system.errorhandler.exception.EntityNotFoundException;
import com.softb.system.errorhandler.exception.FormValidationError;
import com.softb.system.security.model.UserAccount;
import com.softb.system.security.repository.UserAccountRepository;

/**
 * Implementation for UserAccountService.
 * 
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {
    private static final Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    public static final String USER_ID_PREFIX = "user";

    @Autowired
    private UserAccountRepository accountRepository;
    
    @Resource
    private Validator validator;

    @Override
    public UserAccount createUserAccount(UserAccount account) {
        // perfil padrão para novos usuários
        account.defineDefaultRoles();
        
        // valida os dados básicos
        validate("UserAccount", account);
        
        // valida se já existe um usuário para o userid/email informado
        if ( !StringUtils.isEmpty(account.getEmail()) ) {
	        UserAccount existing = accountRepository.findByEmail(account.getEmail());
	        if (existing != null) { 
	        	throw new BusinessException("Já existe um usuário com o email informado. Altere o seu email e tente novamente.");
	        }
        }
        
        account = accountRepository.save(account);
        
        logger.info(String.format("A new user is created (userId='%s') for '%s'.", account.getUserId(), account.getDisplayName()));
        return account;
    }

    @Override
    public UserAccount grant(String userId, String role) throws UsernameNotFoundException {
        UserAccount account = loadUserByUserId(userId);
        account.getRoles().add(role);
        return this.accountRepository.save(account);
    }
    
	@Override
	public UserAccount revoke(String userId, String role) {
        UserAccount account = loadUserByUserId(userId);
        account.getRoles().remove(role);
        return this.accountRepository.save(account);
	}
    

    @Override
    public UserAccount loadUserByUserId(String userId) throws UsernameNotFoundException {
    	// quebra o padrao de user:id
    	String[] tokens = userId.split(":");
    	UserAccount account = accountRepository.findOne(Integer.parseInt(tokens[1]));
        if (account == null) {
            throw new EntityNotFoundException("Cannot find user by userId " + userId);
        } 
        return account;
    }

    @Override
    public UserAccount loadUserByUsername(String username) throws UsernameNotFoundException {
    	UserAccount account = accountRepository.findByEmail(username);
        if (account == null) {
            throw new EntityNotFoundException("Cannot find user by email " + username);
        }
        return account;
    }

    @Override
    public UserAccount getCurrentUser() {
    	Integer userId = getUserId();
    	if (userId != null ) {
        	return accountRepository.findOne(userId);
    	} else {
    		return null;
    	}
    }
    
    private void validate(String objectName, Object validated) throws FormValidationError {
    	logger.debug("Validating object: " + validated);

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(validated, objectName);
        validator.validate(validated, bindingResult);

        if (bindingResult.hasErrors()) {
        	logger.debug("Validation errors found:" + bindingResult.getFieldErrors());
            throw new FormValidationError(bindingResult.getFieldErrors());
        }
    }
    
    private Integer getUserId() {
    	String userId = getUserName();
    	if (!StringUtils.isEmpty(userId)) {
        	String[] tokens = userId.split(":");
        	if (tokens.length == 2) {
        		return NumberUtils.isNumber(tokens[1]) ? Integer.parseInt(tokens[1]) : null;
        	}
    	} 
    	
    	// retorna null caso não consiga identificar o UserId
    	return null;

    }
    
    private String getUserName() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	return (authentication != null) ? authentication.getName() : null;
    }
}
