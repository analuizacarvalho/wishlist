package br.com.luizalabs.wishlist.infrastructure.gateways;

import br.com.luizalabs.wishlist.domain.entity.Product;
import br.com.luizalabs.wishlist.infrastructure.persistence.ProductDocument;

public class ProductDocumentMapper {

    Product toProduct(ProductDocument productDocument) {
        return new Product(productDocument.getId(), productDocument.getSku(), productDocument.getSeller(), productDocument.getTitle(), productDocument.getDescription(), productDocument.getImageUrl(), productDocument.getStatus().name());
    }
}
