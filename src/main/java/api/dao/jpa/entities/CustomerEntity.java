package api.dao.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String role;
    @Column(name = "created_at")
    private String createdAt;
    @Column(name = "updated_at")
    private String updated_at;

}
