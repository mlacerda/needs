package com.softb.system.security.web;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softb.system.security.model.UserAccount;
import com.softb.system.security.model.UserSocialConnection;
import com.softb.system.security.repository.UserAccountRepository;
import com.softb.system.security.repository.UserSocialConnectionRepository;
import com.softb.system.security.service.UserAccountService;
import com.softb.system.security.web.resource.UserAccountResource;


/**
 * RESTful Service para gerenciar os usuários do sistema.
 * 
 * <p>API: '<b>api/admin/users/:action:userId/:userAction</b>'</p>
 * <p><b>:action</b> can be
 * <ul>
 * <li>'list' - retorna todos os usuários do sistema.</li>
 * </ul>
 * </p>
 * <p><b>:userAction</b> can be
 * <ul>
 * <li>'{userId}/lock' - lock user account.</li>
 * <li>'{userId}/unlock' - unlock user account.</li>
 * <li>'{userId}/trust' - set user as trusted.</li>
 * <li>'{userId}/untrust' - set user as not trusted.</li>
 * <li>'{userId}/grant/{role}' - add a new role</li>
 * <li>'{userId}/revoke/{role} - remove an existing role'</li> * 
 * </ul>
 * </p>
 */
@Controller
@RequestMapping("/api/admin/users")
public class AccountAdminController {
	private static final Logger logger = LoggerFactory.getLogger(AccountAdminController.class);
	
	private final UserAccountRepository userAccountRepository;
	private final UserSocialConnectionRepository userSocialConnectionRepository;
	private final UserAccountService userAccountService;
	
    @Inject
    public AccountAdminController(UserAccountRepository userAccountRepository,
            UserSocialConnectionRepository userSocialConnectionRepository, UserAccountService userAccountService) {
        this.userAccountRepository = userAccountRepository;
        this.userSocialConnectionRepository = userSocialConnectionRepository;
        this.userAccountService = userAccountService;
    }

    @RequestMapping(value = "/list" , method = RequestMethod.GET)
    @ResponseBody
    public List<UserAccountResource> listUsers() {
    	logger.debug("==>ManageUserController.listUsers()");
    	List<UserAccountResource> result = new ArrayList<UserAccountResource>();
        List<UserAccount> users = userAccountRepository.findAll(new Sort(Direction.ASC, "email"));
        
        for (UserAccount userAccount : users){
            List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(userAccount.getUserId());
            result.add(UserAccountResource.transferForAccountOverview(userAccount, connections)); 
        }
        return result;
    }

    @RequestMapping(value = "/{userId}/lock", method = RequestMethod.PUT)
    @ResponseBody
    public UserAccountResource lockUser(@PathVariable("userId") String userId) {
        UserAccount userAccount = userAccountService.loadUserByUserId(userId);

        userAccount.setAccountLocked(true);
        userAccount = userAccountRepository.save(userAccount);
        List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(userAccount.getUserId());
        return UserAccountResource.transferForAccountOverview(userAccount, connections);
    }

    @RequestMapping(value = "/{userId}/unlock", method = RequestMethod.PUT)
    @ResponseBody
    public UserAccountResource unlockUser(@PathVariable("userId") String userId) {
        UserAccount userAccount = userAccountService.loadUserByUserId(userId);

        userAccount.setAccountLocked(false);
        userAccount = userAccountRepository.save(userAccount);
        List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(userAccount.getUserId());
        return UserAccountResource.transferForAccountOverview(userAccount, connections);
    }

    @RequestMapping(value = "/{userId}/trust", method = RequestMethod.PUT)
    @ResponseBody
    public  UserAccountResource trustUser(@PathVariable("userId") String userId) {
        UserAccount userAccount = userAccountService.loadUserByUserId(userId);

        userAccount.setTrustedAccount(true);
        userAccount = userAccountRepository.save(userAccount);
        List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(userAccount.getUserId());
        return UserAccountResource.transferForAccountOverview(userAccount, connections);
    }

    @RequestMapping(value = "/{userId}/untrust", method = RequestMethod.PUT)
    @ResponseBody
    public UserAccountResource untrustUser(@PathVariable("userId") String userId) {
        UserAccount userAccount = userAccountService.loadUserByUserId(userId);

        userAccount.setTrustedAccount(false);
        userAccount = userAccountRepository.save(userAccount);
        List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(userAccount.getUserId());
        return UserAccountResource.transferForAccountOverview(userAccount, connections);
    }
    
    @RequestMapping(value="/{userId}/grant/{role}", method=RequestMethod.GET)
    @ResponseBody
    public UserAccountResource grant(@PathVariable String userId, @PathVariable String role) {
	
    	UserAccount userAccount = userAccountService.grant(userId,  role);
        List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(userAccount.getUserId());

		return UserAccountResource.transferForAccountOverview(userAccount, connections);

    }
    
    @RequestMapping(value="/{userId}/revoke/{role}", method=RequestMethod.GET)
    @ResponseBody
    public UserAccountResource revoke(@PathVariable String userId, @PathVariable String role) {

    	UserAccount userAccount = userAccountService.revoke(userId,  role);
        List<UserSocialConnection> connections = userSocialConnectionRepository.findByUserId(userAccount.getUserId());

		return UserAccountResource.transferForAccountOverview(userAccount, connections);
    }

}
