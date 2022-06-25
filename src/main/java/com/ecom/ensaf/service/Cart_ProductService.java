package com.ecom.ensaf.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.ensaf.model.Cart;
import com.ecom.ensaf.model.Cart_Product;
import com.ecom.ensaf.model.Product;
import com.ecom.ensaf.repository.CartRepository;
import com.ecom.ensaf.repository.Cart_ProductRepository;

@Service
@Transactional
public class Cart_ProductService {

	@Autowired
	private Cart_ProductRepository cart_ProductRepository;
	
	@Autowired
	private CartRepository cartRepository;

	public boolean isProductInCart(Product product, Cart cart) {
		Cart_Product cart_Product = cart_ProductRepository.getCart_ProductByProduct(product, cart);
		return cart_Product != null;
	}

	public List<Cart_Product> getCart_ProductByCart(Cart cart) {
		return cart_ProductRepository.getCart_ProductByCart(cart);
	}
	
	public void updateCartItem(Integer itemId, Integer quantity, double total) {
		Cart_Product cart_Product = cart_ProductRepository.findById(itemId).get();
		
		Cart cart = cart_Product.getCart();
		cart.setTotal(cart.getTotal() - cart_Product.getTotal());
		
		cart_Product.setQuantity(quantity);
		cart_Product.setTotal(total);
		
		cart.setTotal(cart.getTotal() + cart_Product.getTotal());
		
		cart_ProductRepository.save(cart_Product);
		cartRepository.save(cart);
		
	}

}
