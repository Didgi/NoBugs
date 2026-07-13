package api.dao.jdbc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomersDao {
    private int id;
    private String username;
    private String password;
    private String name;
    private String role;
    private String createdAt;
    private String updatedAt;
}
