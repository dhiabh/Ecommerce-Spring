package com.ecom.ensaf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecom.ensaf.model.Product;

@Repository
public interface Productrepository extends JpaRepository<Product, Integer> {
	
	@Query("select p from Product p where p.name like %:key%")
	List<Product> getProductByKey(String key);

}
