package com.Shaik.SpringEcom.controller;

import com.Shaik.SpringEcom.model.Product;
import com.Shaik.SpringEcom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("products")
    public ResponseEntity<List<Product>> getProducts(){
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK) ;
    }

    @GetMapping("/product/{Id}")
    public ResponseEntity<Product> getProductById(@PathVariable("Id") int Id){
        Product product = service.getProductById(Id);
        if(product != null){
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) {
        Product savedProduct = null;
        try {
            savedProduct = service.addOrupdateProduct(product, imageFile);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{Id}/image")
    public ResponseEntity<byte[]> getImageByProductById(@PathVariable int Id){
        Product p = service.getProductById(Id);
        return new ResponseEntity<>(p.getImageData(),HttpStatus.OK);

    }

    @PutMapping("/product/{Id}")
    public ResponseEntity<String> updateProduct(@PathVariable int Id,
                                                @RequestPart("product") Product product,
                                                @RequestPart("imageFile") MultipartFile imageFile ) throws IOException {
        Product p = service.addOrupdateProduct(product,imageFile);
        return new ResponseEntity<>("Updated",HttpStatus.OK);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        Product product = service.getProductById(id);
        if (product != null) {
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = service.searchProducts(keyword);
        System.out.println("searching with :" + keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
