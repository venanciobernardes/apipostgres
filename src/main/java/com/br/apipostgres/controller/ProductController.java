package com.br.apipostgres.controller;

import com.br.apipostgres.exception.ResourceNotFoundException;
import com.br.apipostgres.model.Product;
import com.br.apipostgres.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product){
        productService.save(product);
        return product;
    }

    @GetMapping("/products/ok")
    public ResponseEntity<String> returnOk(){
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") Integer productId){
        Product product=productService.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found" + productId));
        return ResponseEntity.ok().body(product);
    }


    @GetMapping("/products")
    public List<Product> getProducts(){
        return productService.findAll();
    }

    @PutMapping("products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") Integer productId,
                                                 @RequestBody Product productDetails) {
        Product product = productService.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + productId));
        product.setName(productDetails.getName());
        product.setColor(productDetails.getColor());
        final Product updatedProduct = productService.save(product);
        return ResponseEntity.ok(updatedProduct);

    }

    @DeleteMapping("products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "id") Integer productId) {
        Product product = productService.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product not found::: " + productId));
        productService.delete(product);
        return ResponseEntity.ok().build();
    }

}
