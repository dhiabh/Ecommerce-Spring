package com.ecom.ensaf.dto.cart;

public class CartItemUpdate {
	
	private Integer quantity;
	private double total;
	
	public CartItemUpdate(Integer quantity, double total) {
		super();
		this.quantity = quantity;
		this.total = total;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	

}
