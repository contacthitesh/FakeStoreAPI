package dev.API.services;

import dev.API.dtos.CreateProductRequestDto;
import dev.API.exceptions.ProductNotFoundException;
import dev.API.models.Category;
import dev.API.models.Product;

import java.util.List;

public interface ProductService {

    Product getSingleProduct(Long productId) throws ProductNotFoundException;

    List<Product> getProducts();
    List<Product> getProducts(String category);

    Product createProduct(String title, String description, String category, double price, String image);

    Product updateProduct(Long productId, Product product);

    Product deleteProduct(Long productId) throws ProductNotFoundException;

    List<Category> getCategories();
}
