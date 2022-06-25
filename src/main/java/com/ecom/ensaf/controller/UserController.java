package com.ecom.ensaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecom.ensaf.common.ApiResponse;
import com.ecom.ensaf.dto.ResponseDto;
import com.ecom.ensaf.dto.user.SignInDto;
import com.ecom.ensaf.dto.user.SignInResponseDto;
import com.ecom.ensaf.dto.user.SignupDto;
import com.ecom.ensaf.dto.user.UserCreateDto;
import com.ecom.ensaf.dto.user.UserUpdateDto;
import com.ecom.ensaf.exceptions.AuthenticationFailException;
import com.ecom.ensaf.exceptions.CustomException;
import com.ecom.ensaf.model.User;
import com.ecom.ensaf.repository.UserRepository;
import com.ecom.ensaf.service.AuthenticationService;
import com.ecom.ensaf.service.UserService;

import java.util.List;

@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthenticationService authenticationService;

	@Autowired
	UserService userService;

	@GetMapping("/all")
	public List<User> findAllUser(@RequestParam("token") String token) throws AuthenticationFailException {
		authenticationService.authenticate(token);
		return userRepository.findAll();
	}
	
	@GetMapping("/getUserByToken")
	public User findUserByToken(@RequestParam("token") String token) throws AuthenticationFailException {
		User user = authenticationService.getUser(token);
		return user;
	}
	
	@GetMapping("/getUserById/{userId}")
	public User findUserById(@RequestParam("token") String token, @PathVariable("userId") int userId)
			throws AuthenticationFailException {
		authenticationService.authenticate(token);
		User user = userService.getUserById(userId);
		return user;
	}

	@PostMapping("/signup")
	public ResponseDto Signup(@RequestBody SignupDto signupDto) throws CustomException {
		return userService.signUp(signupDto);
	}

	// TODO token should be updated
	@PostMapping("/signIn")
	public SignInResponseDto Signup(@RequestBody SignInDto signInDto) throws CustomException {
		return userService.signIn(signInDto);
	}


    @PostMapping("/updateUser")
    public ResponseDto updateUser(@RequestParam("token") String token, @RequestBody UserUpdateDto userUpdateDto)
            throws CustomException, AuthenticationFailException {
        authenticationService.authenticate(token);
        return userService.updateUser(token ,userUpdateDto);
    }
    
    @PostMapping("/delete/{userId}")
	public ResponseEntity<ApiResponse> deleteProduct(@RequestParam("token") String token,
			@PathVariable("userId") Integer userId) {
		userService.deleteUser(token, userId);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "User Deleted Successfully"), HttpStatus.OK);
	}
}
