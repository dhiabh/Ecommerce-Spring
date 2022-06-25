package com.ecom.ensaf.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.ensaf.common.ApiResponse;
import com.ecom.ensaf.dto.product.ProductDto;
import com.ecom.ensaf.model.Category;
import com.ecom.ensaf.model.Product;
import com.ecom.ensaf.service.CategoryService;
import com.ecom.ensaf.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	ProductService productService;
	@Autowired
	CategoryService categoryService;

	@GetMapping("/")
	public ResponseEntity<List<ProductDto>> getProducts() {
		List<ProductDto> body = productService.listProducts();
		return new ResponseEntity<List<ProductDto>>(body, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto productDto) {
		Optional<Category> optionalCategory = categoryService.readCategory(productDto.getCategoryId());
		if (!optionalCategory.isPresent()) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category is invalid"), HttpStatus.CONFLICT);
		}
		Category category = optionalCategory.get();
		productService.addProduct(productDto, category);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Product has been added"), HttpStatus.CREATED);
	}

	@PostMapping("/update/{productID}")
	public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productID") Integer productID,
			@RequestBody @Valid ProductDto productDto) {
		Optional<Category> optionalCategory = categoryService.readCategory(productDto.getCategoryId());
		if (!optionalCategory.isPresent()) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category is invalid"), HttpStatus.CONFLICT);
		}
		Category category = optionalCategory.get();
		productService.updateProduct(productID, productDto, category);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);
	}
	
	@GetMapping("/searchWithKey")
	public ResponseEntity<List<ProductDto>> getProductsByKey(@RequestParam(value = "key") String key) {
		List<ProductDto> body = productService.searchProductWithKey(key);
		return new ResponseEntity<List<ProductDto>>(body, HttpStatus.OK);
	}

	
	@PostMapping("/delete/{productID}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("productID") Integer productID) {
		productService.deleteProduct(productID);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);
	}

}
