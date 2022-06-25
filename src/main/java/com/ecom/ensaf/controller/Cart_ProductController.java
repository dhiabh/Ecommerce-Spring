package com.ecom.ensaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.ensaf.model.Cart;
import com.ecom.ensaf.model.Product;
import com.ecom.ensaf.model.User;
import com.ecom.ensaf.service.AuthenticationService;
import com.ecom.ensaf.service.Cart_ProductService;

@RestController
@RequestMapping("/cart_product")
public class Cart_ProductController {

	@Autowired
	Cart_ProductService cart_ProductService;

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/")
	public ResponseEntity<Boolean> isProductInCart(@RequestBody Product product, @RequestParam("token") String token) {
		authenticationService.authenticate(token);
		User user = authenticationService.getUser(token);
		Cart cart = user.getCart();
		boolean productInCart = cart_ProductService.isProductInCart(product, cart);
		return new ResponseEntity<Boolean>(productInCart, HttpStatus.OK);

	}

}
