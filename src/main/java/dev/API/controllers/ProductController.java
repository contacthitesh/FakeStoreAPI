package dev.API.controllers;

import dev.API.dtos.CreateProductRequestDto;
import dev.API.exceptions.ProductNotFoundException;
import dev.API.models.Category;
import dev.API.models.Product;
import dev.API.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ProductController {

//    private Map<Integer, Integer> mp = new TreeMap<>();
//    List<Integer> li = new ArrayList<>();

    private ProductService productService;
    private RestTemplate restTemplate;

    public ProductController(ProductService productService,
                             RestTemplate restTemplate) {
        this.productService = productService;
        this.restTemplate = restTemplate;
    }
    //1
    @PostMapping("/products")
    public Product createProduct(@RequestBody CreateProductRequestDto request) {
        return productService.createProduct(
                request.getTitle(),
                request.getDescription(),
                request.getCategory(),
                request.getPrice(),
                request.getImage()
        );
    }

    //2
    @GetMapping("/products/{id}")
    public Product getProductDetails(@PathVariable("id") Long productId) throws ProductNotFoundException {
        return productService.getSingleProduct(productId);
    }

    //3
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() throws ProductNotFoundException {
        List<Product> products = productService.getProducts();
        ResponseEntity<List<Product>> response = new ResponseEntity<>(products, HttpStatus.NOT_FOUND);
        return response;
    }

    //4
    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable("id") Long productId, @RequestBody CreateProductRequestDto request){
        Product product = new Product();
        product.setDescription(request.getDescription());
        Category category = new Category();
        category.setTitle(request.getCategory());
        product.setCategory(category);
        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        return productService.updateProduct(productId,product);
    }

    //5
    @DeleteMapping("/products/{productId}")
    public Product deleteProduct(@PathVariable("productId") Long productId) throws ProductNotFoundException {

        return productService.deleteProduct(productId);
    }

    //6
    @GetMapping("/products/category/{category}")
    public List<Product> getProducts(@PathVariable("category") String category) {
        return productService.getProducts(category);
    }

    //7
    @GetMapping("/products/categories")
    public List<Category> getCategories() {
        return productService.getCategories();
    }



}

