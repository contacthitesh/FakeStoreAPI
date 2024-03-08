package dev.API.services;

import  dev.API.dtos.FakeStoreProductDto;
import  dev.API.exceptions.ProductNotFoundException;
import dev.API.models.Category;
import  dev.API.models.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class FakeStoreProductService implements ProductService {
    // RestTemplate
    // - allows to send HTTP requests to external URLs
    //    and work with responses

    private RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {
        ResponseEntity<FakeStoreProductDto> fakeStoreProductResponse = restTemplate.getForEntity(
                "https://fakestoreapi.com/products/" + productId,
                FakeStoreProductDto.class
        );
        FakeStoreProductDto fakeStoreProduct = fakeStoreProductResponse.getBody();
        if (fakeStoreProduct == null) {
            throw new ProductNotFoundException("Product with id: " + productId + " doesn't exist. Retry some other product.");
        }
        return fakeStoreProduct.toProduct();
    }

    @Override
    public List<Product> getProducts() {
        FakeStoreProductDto[] fakeStoreProducts =
                restTemplate.getForObject(
                        "https://fakestoreapi.com/products",
                        FakeStoreProductDto[].class
                );

        List<Product> products = new ArrayList<>();

        for (FakeStoreProductDto fakeStoreProduct: fakeStoreProducts) {
            products.add(fakeStoreProduct.toProduct());
        }

        return products;
    }

    @Override
    public List<Product> getProducts(String category) {

        List<Product> listProduct = new ArrayList<Product>();
        List<LinkedHashMap<String, String>> listFakeStoreProductDto = restTemplate.getForObject("https://fakestoreapi.com/products/category/" + category, List.class);
        for (LinkedHashMap<String, String> linkedHashMap : listFakeStoreProductDto) {
            Product tempProduct = new Product();
            tempProduct.setId((long) Integer.parseInt((String.valueOf(linkedHashMap.get("id")))));
            tempProduct.setDescription(linkedHashMap.get("description"));
            tempProduct.setTitle(linkedHashMap.get("title"));
            tempProduct.setPrice(Double.parseDouble(String.valueOf(linkedHashMap.get("price"))));
            Category tempCategory = new Category();
            tempCategory.setTitle(linkedHashMap.get("category"));
            tempProduct.setCategory(tempCategory);
            tempProduct.setImage(linkedHashMap.get("image"));
            listProduct.add(tempProduct);

        }

        return listProduct;

    }

    // Other similar way of getting the products
    public List<Product> getProducts1() {
        List<Product> listProduct = new ArrayList<Product>();
        List<LinkedHashMap<String, String>> listFakeStoreProductDto = restTemplate.getForObject("https://fakestoreapi.com/products", List.class);
        assert listFakeStoreProductDto != null;
        for (LinkedHashMap<String, String> linkedHashMap : listFakeStoreProductDto) {
            Product tempProduct = new Product();
            tempProduct.setId((long) Integer.parseInt((String.valueOf(linkedHashMap.get("id")))));
            tempProduct.setDescription(linkedHashMap.get("description"));
            tempProduct.setTitle(linkedHashMap.get("title"));
            tempProduct.setPrice(Double.parseDouble(String.valueOf(linkedHashMap.get("price"))));
            Category tempCategory = new Category();
            tempCategory.setTitle(linkedHashMap.get("category"));
            tempProduct.setCategory(tempCategory);
            tempProduct.setImage(linkedHashMap.get("image"));
            listProduct.add(tempProduct);

        }
        return listProduct;
    }
    @Override
    public Product createProduct(String title,String description,String category,double price,String image) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setTitle(title);
        fakeStoreProductDto.setCategory(category);
        fakeStoreProductDto.setPrice(price);
        fakeStoreProductDto.setImage(image);
        fakeStoreProductDto.setDescription(description);

        FakeStoreProductDto response = restTemplate.postForObject(
                "https://fakestoreapi.com/products", // url
                fakeStoreProductDto, // request body
                FakeStoreProductDto.class // data type of response
        );

        if (response == null) return new Product();

        return response.toProduct();
    }

    @Override
    public Product updateProduct(Long productId, Product product) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", productId.toString());
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setId((long) productId.intValue());
        fakeStoreProductDto.setTitle(product.getTitle());
        fakeStoreProductDto.setPrice(Double.valueOf(product.getPrice()).longValue());
        fakeStoreProductDto.setCategory(product.getCategory().getTitle());
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setImage(product.getImage());
        restTemplate.put("https://fakestoreapi.com/products/" + product.getId(), fakeStoreProductDto, params);
        return product;
    }

    @Override
    public Product deleteProduct(Long productId) throws ProductNotFoundException{
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", productId.toString());
        Product product = getSingleProduct(productId);
        restTemplate.delete("https://fakestoreapi.com/products/" + productId, params);
        return product;
    }
    @Override
    public List<Category> getCategories() {
        List<String> fakeStoreCategoryDtoList = restTemplate.getForObject("https://fakestoreapi.com/products/categories", List.class);
        List<Category> categoryList = new ArrayList<Category>();
        assert (fakeStoreCategoryDtoList != null ? fakeStoreCategoryDtoList : null) != null;
        for (String fakeStoreCategoryDto : fakeStoreCategoryDtoList != null ? fakeStoreCategoryDtoList : null) {
            Category tempCategory = new Category();
            tempCategory.setTitle(fakeStoreCategoryDto);
            categoryList.add(tempCategory);
        }
        return categoryList;
    }
}
