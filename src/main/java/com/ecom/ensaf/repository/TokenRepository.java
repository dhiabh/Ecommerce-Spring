package com.ecom.ensaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.ensaf.model.AuthenticationToken;
import com.ecom.ensaf.model.User;

@Repository
public interface TokenRepository extends JpaRepository<AuthenticationToken, Integer> {
	AuthenticationToken findTokenByUser(User user);

	AuthenticationToken findTokenByToken(String token);
}
