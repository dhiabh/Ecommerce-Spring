package com.ecom.ensaf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.ensaf.dto.product.ProductDto;
import com.ecom.ensaf.exceptions.ProductNotExistException;
import com.ecom.ensaf.model.Category;
import com.ecom.ensaf.model.Product;
import com.ecom.ensaf.repository.Productrepository;

@Service
public class ProductService {

	@Autowired
	private Productrepository productRepository;

	public List<ProductDto> listProducts() {
		List<Product> products = productRepository.findAll();
		List<ProductDto> productDtos = new ArrayList<>();
		for (Product product : products) {
			ProductDto productDto = getDtoFromProduct(product);
			productDtos.add(productDto);
		}
		return productDtos;
	}

	public static ProductDto getDtoFromProduct(Product product) {
		ProductDto productDto = new ProductDto(product);
		return productDto;
	}

	public static Product getProductFromDto(ProductDto productDto, Category category) {
		Product product = new Product(productDto, category);
		return product;
	}

	public void addProduct(ProductDto productDto, Category category) {
		Product product = getProductFromDto(productDto, category);
		productRepository.save(product);
	}

	public void updateProduct(Integer productID, ProductDto productDto, Category category) {
		Product product = getProductFromDto(productDto, category);
		product.setId(productID);
		productRepository.save(product);
	}

	public Product getProductById(Integer productId) throws ProductNotExistException {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		if (!optionalProduct.isPresent())
			throw new ProductNotExistException("Product id is invalid " + productId);
		return optionalProduct.get();
	}
	
	public List<ProductDto> searchProductWithKey(String key) {
		List<Product> products = productRepository.getProductByKey(key);
		List<ProductDto> productsDto = new ArrayList<ProductDto>();
		for(Product product : products) {
			productsDto.add(getDtoFromProduct(product));
		}

		return productsDto;
	}
	
	public void deleteProduct(Integer productId) throws ProductNotExistException {
		productRepository.deleteById(productId);
	}

}
