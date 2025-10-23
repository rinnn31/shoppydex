package com.github.rinnn31.shoppydex.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rinnn31.shoppydex.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {
    @Autowired 
    private ProductService productService;


}
