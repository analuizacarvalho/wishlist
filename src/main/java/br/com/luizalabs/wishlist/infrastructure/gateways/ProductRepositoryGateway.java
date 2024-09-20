package br.com.luizalabs.wishlist.infrastructure.gateways;

import br.com.luizalabs.wishlist.application.gateways.ProductGateway;
import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.infrastructure.persistence.ProductDocument;
import br.com.luizalabs.wishlist.infrastructure.persistence.ProductRepository;

import java.util.List;
import java.util.Optional;

public class ProductRepositoryGateway implements ProductGateway {

    private final ProductRepository productRepository;
    private final ProductDocumentMapper productDocumentMapper;

    public ProductRepositoryGateway(ProductRepository productRepository,
                                    ProductDocumentMapper productDocumentMapper) {
        this.productRepository = productRepository;
        this.productDocumentMapper = productDocumentMapper;
    }

    @Override
    public List<Product> findAllProducts() {
        List<ProductDocument> productsList = productRepository.findAll();
        return productsList.stream().map(productDocumentMapper::toProduct).toList();
    }

    @Override
    public List<Product> findAllProductsByIdList(List<String> productIdList) {
        List<ProductDocument> productsList = productRepository.findAllById(productIdList);
        return productsList.stream().map(productDocumentMapper::toProduct).toList();
    }

    @Override
    public Optional<Product> findProductById(String productId) {
        return productRepository.findById(productId).map(productDocumentMapper::toProduct);
    }
}
