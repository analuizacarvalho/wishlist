package br.com.luizalabs.wishlist.application.usecases;

import br.com.luizalabs.wishlist.application.exception.UseCaseException;
import br.com.luizalabs.wishlist.application.gateways.ProductGateway;
import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.infrastructure.adapters.MessageHelper;

import java.util.List;

public class ProductInteractor {

    private final ProductGateway productGateway;
    private final MessageHelper messageHelper;

    public ProductInteractor(ProductGateway productGateway,
                             MessageHelper messageHelper) {
        this.productGateway = productGateway;
        this.messageHelper = messageHelper;
    }

    public List<Product> getAllProducts() {
        try {
            return productGateway.findAllProducts();
        } catch (Exception ex) {
            throw new UseCaseException(messageHelper.getMessage("error.find.product"));
        }
    }
}
