package com.shopping.demo.Service;

import com.shopping.demo.DAO.ProductRepository;
import com.shopping.demo.Entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    public final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product){

        return productRepository.save(product);
    }

    public List<Product> getAllProducts(){

        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public void deleteProductById(Integer id) {
        productRepository.deleteById(id);
    }
    }
