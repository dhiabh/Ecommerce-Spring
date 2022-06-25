package com.ecom.ensaf.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ecom.ensaf.model.Category;
import com.ecom.ensaf.model.Product;
import com.ecom.ensaf.repository.Categoryrepository;

@Service
@Transactional
public class CategoryService {

	private final Categoryrepository categoryrepository;
	private final ProductService productService;

	public CategoryService(Categoryrepository categoryrepository, ProductService productService) {
		this.categoryrepository = categoryrepository;
		this.productService = productService;
	}

	public List<Category> listCategories() {
		return categoryrepository.findAll();
	}

	public void createCategory(Category category) {
		categoryrepository.save(category);
	}
	

	public Category readCategory(String categoryName) {
		return categoryrepository.findByCategoryName(categoryName);
	}

	public Optional<Category> readCategory(Integer categoryId) {
		return categoryrepository.findById(categoryId);
	}

	public void updateCategory(Integer categoryID, Category newCategory) {
		Category category = categoryrepository.findById(categoryID).get();
		category.setCategoryName(newCategory.getCategoryName());
		category.setDescription(newCategory.getDescription());
		category.setProducts(newCategory.getProducts());
		category.setImageUrl(newCategory.getImageUrl());

		categoryrepository.save(category);
	}
	
	public void deleteCategory(Integer categoryID) {
		Category category = categoryrepository.findById(categoryID).get();
		for(Product product : category.getProducts() ) {
			productService.deleteProduct(product.getId());
		}
		
		categoryrepository.deleteById(categoryID);
		
	}
}
