package api.db.jdbc.mapper;

import api.dao.jdbc.CustomersDao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomersMapper implements RowMapper<CustomersDao> {

    @Override
    public CustomersDao map(ResultSet resultSet)
            throws SQLException {

        return CustomersDao.builder()
                .id(resultSet.getInt("id"))
                .username(resultSet.getString("username"))
                .password(resultSet.getString("password"))
                .name(resultSet.getString("name"))
                .role(resultSet.getString("role"))
                .createdAt(resultSet.getString("created_at"))
                .updatedAt(resultSet.getString("updated_at"))
                .build();
    }
}
