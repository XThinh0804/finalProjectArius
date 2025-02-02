package vn.arius.finalProject.entity.elasticsearch;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import vn.arius.finalProject.entity.Role;

import java.io.Serializable;

@Document(indexName = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDocument implements Serializable {
    @Id
    private String id;

    @Field(name = "name", type = FieldType.Text)
    private String name;
    @Field(name = "email", type = FieldType.Text)
    private String email;
    @Field(name = "address", type = FieldType.Text)
    private String address;
    @Field(name = "phone", type = FieldType.Text)
    private String phone;
    @Field(name = "role", type = FieldType.Object)
    private EsRole role;
}
