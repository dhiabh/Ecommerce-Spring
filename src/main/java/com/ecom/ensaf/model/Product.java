package com.ecom.ensaf.model;

import com.ecom.ensaf.dto.product.ProductDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "products")
public class Product implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private @NotNull String name;
	private @NotNull String imageURL;
	private @NotNull double price;
	private @NotNull String description;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id", nullable = false)
	Category category;


	@JsonIgnore
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<Cart_Product> cart_products;
    
	public Product(ProductDto productDto, Category category) {
		this.name = productDto.getName();
		this.imageURL = productDto.getImageURL();
		this.description = productDto.getDescription();
		this.price = productDto.getPrice();
		this.category = category;
	}


	public Product() {
	}


	public Product(Integer id, @NotNull String name, @NotNull String imageURL, @NotNull double price,
			@NotNull String description, Category category, List<Cart_Product> cart_products) {
		super();
		this.id = id;
		this.name = name;
		this.imageURL = imageURL;
		this.price = price;
		this.description = description;
		this.category = category;
		this.cart_products = cart_products;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getImageURL() {
		return imageURL;
	}


	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Category getCategory() {
		return category;
	}


	public void setCategory(Category category) {
		this.category = category;
	}


	public List<Cart_Product> getCart_products() {
		return cart_products;
	}


	public void setCart_products(List<Cart_Product> cart_products) {
		this.cart_products = cart_products;
	}


	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", imageURL=" + imageURL + ", price=" + price + ", description="
				+ description + ", category=" + category + ", cart_products=" + cart_products + "]";
	}
	
	

	
}
