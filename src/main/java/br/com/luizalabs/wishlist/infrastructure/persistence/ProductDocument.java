package br.com.luizalabs.wishlist.infrastructure.persistence;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class ProductDocument {
    @Id
    private String id;
    private String sku;
    private String seller;
    private String title;
    private String description;
    private String imageUrl;
    private ProductStatus status;
}
