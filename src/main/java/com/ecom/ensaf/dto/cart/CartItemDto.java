package com.ecom.ensaf.dto.cart;

import javax.validation.constraints.NotNull;

import com.ecom.ensaf.model.Cart;
import com.ecom.ensaf.model.Product;

public class CartItemDto {
	private Integer id;
	private @NotNull Integer product_id;
	private @NotNull Integer cart_id;
	private @NotNull double total;
	private @NotNull Integer quantity;

	

	public CartItemDto() {
	}



	public CartItemDto(Integer id, @NotNull Integer product_id, @NotNull Integer cart_id, @NotNull double total,
			@NotNull Integer quantity) {
		super();
		this.id = id;
		this.product_id = product_id;
		this.cart_id = cart_id;
		this.total = total;
		this.quantity = quantity;
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getProduct_id() {
		return product_id;
	}



	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}



	public Integer getCart_id() {
		return cart_id;
	}



	public void setCart_id(Integer cart_id) {
		this.cart_id = cart_id;
	}



	public double getTotal() {
		return total;
	}



	public void setTotal(double total) {
		this.total = total;
	}



	public Integer getQuantity() {
		return quantity;
	}



	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	

}