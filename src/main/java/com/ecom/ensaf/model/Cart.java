package com.ecom.ensaf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cart")
public class Cart implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_date")
	private Date createdDate;

	@JsonIgnore
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;
	
	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Cart_Product> cart_products;

	private int products_number;
	
	private double total;

	public Cart() {
	}

	

	public Cart(Integer id, Date createdDate, User user, List<Cart_Product> cart_products, int products_number,
			double total) {
		this.id = id;
		this.createdDate = createdDate;
		this.user = user;
		this.cart_products = cart_products;
		this.products_number = products_number;
		this.total = total;
	}
	
	



	public Cart(User user) {
		this.user = user;
		this.createdDate = new Date();
		this.products_number = 0;
		this.total = 0;

	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Cart_Product> getCart_products() {
		return cart_products;
	}

	public void setCart_products(List<Cart_Product> cart_products) {
		this.cart_products = cart_products;
	}

	public int getProducts_number() {
		return products_number;
	}

	public void setProducts_number(int products_number) {
		this.products_number = products_number;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}



	@Override
	public String toString() {
		return "Cart [id=" + id + ", createdDate=" + createdDate + ", user=" + user + ", cart_products=" + cart_products
				+ ", products_number=" + products_number + ", total=" + total + "]";
	}

	
	
}