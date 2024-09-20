package br.com.luizalabs.wishlist.infrastructure.controllers;

import br.com.luizalabs.wishlist.application.usecases.ProductInteractor;
import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.ProductResponse;
import br.com.luizalabs.wishlist.infrastructure.controllers.mapper.ProductDTOMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductInteractor productInteractor;
    private final ProductDTOMapper productDTOMapper;

    public ProductController(ProductInteractor productInteractor,
                             ProductDTOMapper productDTOMapper) {
        this.productInteractor = productInteractor;
        this.productDTOMapper = productDTOMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productInteractor.getAllProducts();
        return ResponseEntity.ok().body(products.stream().map(productDTOMapper::toProductResponse).toList());
    }

}
