package com.ecom.ensaf.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ecom.ensaf.model.Cart;
import com.ecom.ensaf.model.Cart_Product;
import com.ecom.ensaf.model.Product;

public interface Cart_ProductRepository extends JpaRepository<Cart_Product, Integer>{

	@Query("select cp from Cart_Product cp where cp.product = ?1 and cp.cart = ?2")
	Cart_Product getCart_ProductByProduct(Product product, Cart cart);
	
	@Query("select cp from Cart_Product cp where cp.cart = ?1")
	List<Cart_Product> getCart_ProductByCart(Cart cart);
	
	@Modifying
	@Query("delete from Cart_Product cp where cp.cart = ?1")
	void deleteAllCartItems(Cart cart);
}
