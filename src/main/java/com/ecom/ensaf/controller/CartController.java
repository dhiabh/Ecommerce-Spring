package com.ecom.ensaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecom.ensaf.common.ApiResponse;
import com.ecom.ensaf.dto.cart.AddToCartDto;
import com.ecom.ensaf.dto.cart.CartDto;
import com.ecom.ensaf.dto.cart.CartItemDto;
import com.ecom.ensaf.dto.cart.CartItemUpdate;
import com.ecom.ensaf.dto.cart.Cart_ProductDto;
import com.ecom.ensaf.dto.product.ProductDto;
import com.ecom.ensaf.exceptions.AuthenticationFailException;
import com.ecom.ensaf.exceptions.CartItemNotExistException;
import com.ecom.ensaf.exceptions.ProductNotExistException;
import com.ecom.ensaf.model.Cart;
import com.ecom.ensaf.model.Cart_Product;
import com.ecom.ensaf.model.Product;
import com.ecom.ensaf.model.User;
import com.ecom.ensaf.service.AuthenticationService;
import com.ecom.ensaf.service.CartService;
import com.ecom.ensaf.service.Cart_ProductService;
import com.ecom.ensaf.service.ProductService;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private CartService cartService;

	@Autowired
	private ProductService productService;

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	Cart_ProductService cart_ProductService;

	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addToCart(@RequestBody Product product,
			@RequestParam("token") String token) throws AuthenticationFailException, ProductNotExistException {
		authenticationService.authenticate(token);
		User user = authenticationService.getUser(token);
		Cart cart = user.getCart();
		
		cartService.addToCart(cart, product);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);

	}

	@GetMapping("/")
	public ResponseEntity<Cart_ProductDto> getCartItems(@RequestParam("token") String token)
			throws AuthenticationFailException {
		authenticationService.authenticate(token);
		User user = authenticationService.getUser(token);
		Cart cart = user.getCart();
		List<Product> products = cartService.listCartItems(cart);
		
		List<ProductDto> productDtos = new ArrayList<ProductDto>();
		
		for(Product product : products) {
			productDtos.add(ProductService.getDtoFromProduct(product));
		}
		
		List<Cart_Product> cart_products = cart_ProductService.getCart_ProductByCart(cart);
		
		List<CartItemDto> cartItemDtos = new ArrayList<>();
		
		for(Cart_Product cart_Product : cart_products ) {
			cartItemDtos.add(new CartItemDto(
					cart_Product.getId(),
					cart_Product.getProduct().getId(),
					cart_Product.getCart().getId(),
					cart_Product.getTotal(),
					cart_Product.getQuantity()
					));
		}
		
	
		
		Cart_ProductDto body = new Cart_ProductDto(productDtos, cart, cartItemDtos);
		
		return new ResponseEntity<Cart_ProductDto>(body, HttpStatus.OK);
	}

	@PutMapping("/update/{cartItemId}")
	public ResponseEntity<ApiResponse> updateCartItem(@PathVariable("cartItemId") int itemID,
			@RequestBody CartItemUpdate cartItemUpdate,
			@RequestParam("token") String token) throws AuthenticationFailException{
		authenticationService.authenticate(token);
		
		cart_ProductService.updateCartItem(itemID, cartItemUpdate.getQuantity(), cartItemUpdate.getTotal());
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);
	}

	@DeleteMapping("/delete/{cartItemId}")
	public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable("cartItemId") int itemID,
			@RequestParam("token") String token) throws AuthenticationFailException, CartItemNotExistException {
		authenticationService.authenticate(token);
		User user = authenticationService.getUser(token);
		Cart cart = user.getCart();
		
		Product product = productService.getProductById(itemID);

		cartService.deleteCartItem(cart, product);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Item has been removed"), HttpStatus.OK);
	}
	
	

}
