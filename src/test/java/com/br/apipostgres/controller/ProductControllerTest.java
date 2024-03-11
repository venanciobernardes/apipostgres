package com.br.apipostgres.controller;
import com.br.apipostgres.exception.ResourceNotFoundException;
import com.br.apipostgres.model.Product;
import com.br.apipostgres.repository.ProductRepository;
import com.br.apipostgres.service.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @MockBean
    private EntityManager entityManager;

    @MockBean
    ProductRepository productRepository;
    @MockBean
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addProduct() {
        Product product = new Product(1, "Produto 1", "Azul");
        given(productService.save(any(Product.class))).willReturn(product);

        Product result = productController.addProduct(product);

        assertNotNull(result);
        assertEquals(product, result);

        then(productService).should().save(any(Product.class));
    }

    @Test
    void returnOk() {
        ResponseEntity<String> result = productController.returnOk();

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("ok", result.getBody());
    }

    @Test
    void findById() {
        Integer productId = 1;
        Product product = new Product(1, "Produto", "Azul");
        given(productService.findById(productId)).willReturn(Optional.of(product));

        ResponseEntity<Product> result = productController.findById(productId);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(product, result.getBody());

        then(productService).should().findById(productId);
    }

    @Test
    void findById_ResourceNotFoundException() {
        Integer productId = 1;
        given(productService.findById(productId)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productController.findById(productId));

        then(productService).should().findById(productId);
    }

    @Test
    void getProducts() {
        List<Product> products = Collections.singletonList(new Product());
        given(productService.findAll()).willReturn(products);

        List<Product> result = productController.getProducts();

        assertNotNull(result);
        assertEquals(products, result);

        then(productService).should().findAll();
    }

    @Test
    void updateProduct() {
        Integer productId = 1;
        Product productDetails = new Product();
        productDetails.setName("UpdatedName");
        productDetails.setColor("UpdatedColor");

        Product existingProduct = new Product();
        given(productService.findById(productId)).willReturn(Optional.of(existingProduct));
        given(productService.save(existingProduct)).willReturn(productDetails);

        ResponseEntity<Product> result = productController.updateProduct(productId, productDetails);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(productDetails, result.getBody());

        then(productService).should().findById(productId);
        then(productService).should().save(existingProduct);
    }

    @Test
    void updateProduct_ResourceNotFoundException() {
        Integer productId = 1;
        Product productDetails = new Product();
        productDetails.setName("UpdatedName");

        given(productService.findById(productId)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productController.updateProduct(productId, productDetails));

        then(productService).should().findById(productId);
    }

    @Test
    void deleteProduct() {
        Integer productId = 1;
        Product product = new Product();
        given(productService.findById(productId)).willReturn(Optional.of(product));

        ResponseEntity<Void> result = productController.deleteProduct(productId);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        then(productService).should().findById(productId);
        then(productService).should().delete(product);
    }

    @Test
    void deleteProduct_ResourceNotFoundException() {
        Integer productId = 1;
        given(productService.findById(productId)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productController.deleteProduct(productId));

        then(productService).should().findById(productId);
    }
}
