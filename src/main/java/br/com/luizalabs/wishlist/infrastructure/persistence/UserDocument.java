package br.com.luizalabs.wishlist.infrastructure.persistence;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndexes({
        @CompoundIndex(name = "email_index", def = "{'email':1}")
})
@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;

}
