package com.ecom.ensaf.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.ensaf.dto.cart.AddToCartDto;
import com.ecom.ensaf.dto.cart.CartDto;
import com.ecom.ensaf.dto.cart.CartItemDto;
import com.ecom.ensaf.exceptions.CartItemNotExistException;
import com.ecom.ensaf.model.Cart;
import com.ecom.ensaf.model.Cart_Product;
import com.ecom.ensaf.model.Product;
import com.ecom.ensaf.model.User;
import com.ecom.ensaf.repository.CartRepository;
import com.ecom.ensaf.repository.Cart_ProductRepository;


@Service
@Transactional
public class CartService {

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private Cart_ProductRepository cart_ProductRepository;

	public CartService() {
	}

	public CartService(CartRepository cartRepository) {
		this.cartRepository = cartRepository;
	}

	public void addToCart(Cart cart , Product product) {
		cart.setProducts_number(cart.getProducts_number() + 1);
		cart.setTotal(cart.getTotal() + product.getPrice());
		Cart_Product cart_Product = new Cart_Product(cart, product, 1, product.getPrice());
		
		cartRepository.save(cart);
		cart_ProductRepository.save(cart_Product);
	}

	public List<Product> listCartItems(Cart cart) {
		List<Cart_Product> cart_Products = cart_ProductRepository.getCart_ProductByCart(cart);
		
		List<Product> products = new ArrayList<Product>();
		
		for(Cart_Product cart_Product : cart_Products) {
			products.add(cart_Product.getProduct());
		}
		 
		return products;
	}

	/*public static CartItemDto getDtoFromCart(Cart cart) {
		return new CartItemDto(cart);
	}

	public void updateCartItem(AddToCartDto cartDto, User user, Product product) {
		Cart cart = cartRepository.getOne(cartDto.getId());
		//cart.setQuantity(cartDto.getQuantity());
		cart.setCreatedDate(new Date());
		cartRepository.save(cart);
	}*/

	public void deleteCartItem(Cart cart, Product product) {
		
		Cart_Product cart_Product = cart_ProductRepository.getCart_ProductByProduct(product, cart);
		
		cart.setProducts_number(cart.getProducts_number() - 1);
		cart.setTotal(cart.getTotal() - cart_Product.getTotal());
		
		cartRepository.save(cart);
		cart_ProductRepository.delete(cart_Product);

	}

	public void deleteAllCartItems(Cart cart) {
		cart_ProductRepository.deleteAllCartItems(cart);
	}
}
