package com.ecom.ensaf.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.ensaf.config.MessageStrings;
import com.ecom.ensaf.dto.ResponseDto;
import com.ecom.ensaf.dto.user.SignInDto;
import com.ecom.ensaf.dto.user.SignInResponseDto;
import com.ecom.ensaf.dto.user.SignupDto;
import com.ecom.ensaf.dto.user.UserCreateDto;
import com.ecom.ensaf.dto.user.UserUpdateDto;
import com.ecom.ensaf.enums.ResponseStatus;
import com.ecom.ensaf.enums.Role;
import com.ecom.ensaf.exceptions.AuthenticationFailException;
import com.ecom.ensaf.exceptions.CustomException;
import com.ecom.ensaf.model.AuthenticationToken;
import com.ecom.ensaf.model.Cart;
import com.ecom.ensaf.model.User;
import com.ecom.ensaf.repository.CartRepository;
import com.ecom.ensaf.repository.UserRepository;
import com.ecom.ensaf.utils.Helper;

import static com.ecom.ensaf.config.MessageStrings.USER_CREATED;


@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CartRepository cartRepository;

	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	CartService cartService;

	Logger logger = LoggerFactory.getLogger(UserService.class);

	public ResponseDto signUp(SignupDto signupDto) throws CustomException {
		// Check to see if the current email address has already been registered.
		if (Helper.notNull(userRepository.findByEmail(signupDto.getEmail()))) {
			// If the email address has been registered then throw an exception.
			throw new CustomException("User already exists");
		}
		// first encrypt the password
		String encryptedPassword = signupDto.getPassword();
		try {
			encryptedPassword = hashPassword(signupDto.getPassword());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error("hashing password failed {}", e.getMessage());
		}

		User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), Role.USER,
				encryptedPassword);

		User createdUser;
		try {
			// save the User
			createdUser = userRepository.save(user);
			Cart cart = new Cart(createdUser);
			cartRepository.save(cart);
			// generate token for user
			final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
			// save token in database
			authenticationService.saveConfirmationToken(authenticationToken);
			// success in creating
			return new ResponseDto(ResponseStatus.success.toString(), USER_CREATED);
		} catch (Exception e) {
			// handle sign up error
			throw new CustomException(e.getMessage());
		}
	}

	public SignInResponseDto signIn(SignInDto signInDto) throws CustomException {
		// first find User by email
		User user = userRepository.findByEmail(signInDto.getEmail());
		if (!Helper.notNull(user)) {
			throw new AuthenticationFailException("user not present");
		}
		try {
			// check if password is right
			if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
				// passowrd doesnot match
				throw new AuthenticationFailException(MessageStrings.WRONG_PASSWORD);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error("hashing password failed {}", e.getMessage());
			throw new CustomException(e.getMessage());
		}

		AuthenticationToken token = authenticationService.getToken(user);

		if (!Helper.notNull(token)) {
			// token not present
			throw new CustomException("token not present");
		}

		return new SignInResponseDto("success", token.getToken());
	}

	String hashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
		String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
		return myHash;
	}

	public ResponseDto createUser(String token, UserCreateDto userCreateDto)
			throws CustomException, AuthenticationFailException {
		User creatingUser = authenticationService.getUser(token);
		if (!canCrudUser(creatingUser.getRole())) {
			// user can't create new user
			throw new AuthenticationFailException(MessageStrings.USER_NOT_PERMITTED);
		}
		String encryptedPassword = userCreateDto.getPassword();
		try {
			encryptedPassword = hashPassword(userCreateDto.getPassword());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error("hashing password failed {}", e.getMessage());
		}

		User user = new User(userCreateDto.getFirstName(), userCreateDto.getLastName(), userCreateDto.getEmail(),
				userCreateDto.getRole(), encryptedPassword);
		User createdUser;
		try {
			createdUser = userRepository.save(user);
			final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
			authenticationService.saveConfirmationToken(authenticationToken);
			return new ResponseDto(ResponseStatus.success.toString(), USER_CREATED);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}

	public ResponseDto updateUser(String token, UserUpdateDto userUpdateDto)
			throws CustomException, AuthenticationFailException {
		User updatingUser = authenticationService.getUser(token);
		if (!canCrudUser(updatingUser.getRole())) {
			// user can't create new user
			throw new AuthenticationFailException(MessageStrings.USER_NOT_PERMITTED);
		}
		
		User currentUser = getUserById(userUpdateDto.getId());

		User user = new User(userUpdateDto.getId() ,userUpdateDto.getFirstName(), userUpdateDto.getLastName(),
				userUpdateDto.getEmail(), userUpdateDto.getRole(), currentUser.getPassword());
		
		try {
			userRepository.save(user);
			return new ResponseDto(ResponseStatus.success.toString(), USER_CREATED);
		} catch (Exception e) {
			// handle user creation fail error
			throw new CustomException(e.getMessage());
		}

	}
	
	public ResponseDto deleteUser(String token, Integer userId) 
			throws CustomException, AuthenticationFailException {
		User deletingUser = authenticationService.getUser(token);
		if (!canCrudUser(deletingUser.getRole())) {
			// user can't create new user
			throw new AuthenticationFailException(MessageStrings.USER_NOT_PERMITTED);
		}
		try {
			User user = userRepository.findById(userId).get();
			
			// delete the token
			AuthenticationToken userToken = authenticationService.getToken(user);
			authenticationService.deleteToken(userToken.getId());
			
			// delete the cart
			Cart cart = user.getCart();
			cartService.deleteAllCartItems(cart);
			cartRepository.delete(cart);
			
		
			userRepository.deleteById(userId);
			return new ResponseDto(ResponseStatus.success.toString(), MessageStrings.USER_DELETED);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		
	}

	
	boolean canCrudUser(Role role) {
		
		return role == Role.ADMIN_USER;
	}

	boolean canCrudUser(User userUpdating, Integer userIdBeingUpdated) {
		Role role = userUpdating.getRole();
		if (role == Role.ADMIN_USER) {
			return true;
		}
		// user can update his own record, but not his role
		if (role == Role.USER && userUpdating.getId() == userIdBeingUpdated) {
			return true;
		}
		return false;
	}

	public User getUserById(int userId) {
		return userRepository.findById(userId).get();
	}
}
