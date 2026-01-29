package com.shopping.demo.Controller;

import com.shopping.demo.DAO.ProductRepository;
import com.shopping.demo.Entity.Product;
import com.shopping.demo.Entity.User;
import com.shopping.demo.Service.ProductService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;

    }

        @PostMapping()
        public ResponseEntity<Product> createProduct(@RequestBody Product product){
            Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

        @GetMapping
        public ResponseEntity<List<Product>> readProduct(){
            List<Product> AllProducts = productService.getAllProducts() ;
            return ResponseEntity.status(HttpStatus.OK) .body(AllProducts);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Product> getProductByID(@PathVariable Integer id){
            Optional<Product> optionalProduct=productService.getProductById(id);
            if(optionalProduct.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(optionalProduct.get());
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        }

        @PostMapping("/{id}")
        public ResponseEntity<Product> updateProduct(@PathVariable Integer id,@RequestBody Product updatedProduct){
            Optional<Product> optionalProduct=productService.getProductById(id);
            if(optionalProduct.isPresent()){
                updatedProduct.setId(id);
                Product savedProduct = productService.saveProduct(updatedProduct);
                return ResponseEntity.status(HttpStatus.OK).body(savedProduct);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Product> deleteProduct(@PathVariable Integer id){
            Optional<Product> optionalProduct=productService.getProductById(id);
            if (optionalProduct.isPresent()) {
                productService.deleteProductById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }


}
