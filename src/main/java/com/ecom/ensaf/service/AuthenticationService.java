package com.ecom.ensaf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.ensaf.config.MessageStrings;
import com.ecom.ensaf.exceptions.AuthenticationFailException;
import com.ecom.ensaf.model.AuthenticationToken;
import com.ecom.ensaf.model.User;
import com.ecom.ensaf.repository.TokenRepository;
import com.ecom.ensaf.utils.Helper;

@Service
public class AuthenticationService {

	@Autowired
	TokenRepository repository;

	public void saveConfirmationToken(AuthenticationToken authenticationToken) {
		repository.save(authenticationToken);
	}

	public AuthenticationToken getToken(User user) {
		return repository.findTokenByUser(user);
	}

	public User getUser(String token) {
		AuthenticationToken authenticationToken = repository.findTokenByToken(token);
		if (Helper.notNull(authenticationToken)) {
			if (Helper.notNull(authenticationToken.getUser())) {
				return authenticationToken.getUser();
			}
		}
		return null;
	}

	public void authenticate(String token) throws AuthenticationFailException {
		if (!Helper.notNull(token)) {
			throw new AuthenticationFailException(MessageStrings.AUTH_TOEKN_NOT_PRESENT);
		}
		if (!Helper.notNull(getUser(token))) {
			throw new AuthenticationFailException(MessageStrings.AUTH_TOEKN_NOT_VALID);
		}
	}
	
	public void deleteToken(Integer id) {
		repository.deleteById(id);
	}
}
