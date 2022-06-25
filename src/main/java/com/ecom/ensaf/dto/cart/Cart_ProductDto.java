package com.ecom.ensaf.dto.cart;

import java.util.List;

import com.ecom.ensaf.dto.product.ProductDto;
import com.ecom.ensaf.model.Cart;

public class Cart_ProductDto {
	
	private List<ProductDto> products;
	private Cart cart;
	private List<CartItemDto> cartItemDtos;
	
	public Cart_ProductDto(List<ProductDto> products, Cart cart, List<CartItemDto> cartItemDtos) {
		super();
		this.products = products;
		this.cart = cart;
		this.cartItemDtos = cartItemDtos;
	}
	
	public List<ProductDto> getProducts() {
		return products;
	}
	public void setProducts(List<ProductDto> products) {
		this.products = products;
	}
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	public List<CartItemDto> getCartItemDtos() {
		return cartItemDtos;
	}
	public void setCartItemDtos(List<CartItemDto> cartItemDtos) {
		this.cartItemDtos = cartItemDtos;
	}
	
	
	
	
	
	
	
	
	

}
