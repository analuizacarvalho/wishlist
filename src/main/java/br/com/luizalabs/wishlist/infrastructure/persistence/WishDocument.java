package br.com.luizalabs.wishlist.infrastructure.persistence;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "user_id_product_id_index", def = "{'userId':1, 'productId':1}")
})
@Document(collection = "wishes")
public class WishDocument {

    @Id
    private String id;
    private String productId;
    private String userId;

}