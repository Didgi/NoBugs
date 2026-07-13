package api.db.jdbc;

import api.config.Config;
import api.db.jdbc.mapper.RowMapper;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DBRequests {

    private RequestType requestType;
    private RequestTable requestTable;
    private List<Condition> conditions;

    public enum RequestType {
        SELECT,
        INSERT,
        UPDATE,
        DELETE
    }

    public enum RequestTable {
        CUSTOMERS,
        ACCOUNTS,
        TRANSACTIONS;
    }

    public <T> T extractOne(RowMapper<T> mapper)
            throws SQLException {

        String sql = buildSql();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            if (conditions != null) {
                for (int i = 0; i < conditions.size(); i++) {
                    ps.setObject(i + 1, conditions.get(i).getValue());
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapper.map(rs);
                }
                return null;
            }
        }
    }

    public <T> List<T> extractList(RowMapper<T> mapper)
            throws SQLException {

        String sql = buildSql();

        List<T> result = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            if (conditions != null) {
                for (int i = 0; i < conditions.size(); i++) {
                    ps.setObject(i + 1, conditions.get(i).getValue());
                }
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    result.add(mapper.map(rs));
                }
            }
        }

        return result;
    }

    private String buildSql() {
        StringBuilder sql = new StringBuilder();

        switch (requestType) {
            case SELECT -> {
                sql.append("Select * FROM ");
                sql.append(requestTable);
                if (conditions != null && !conditions.isEmpty()) {
                    sql.append(" WHERE ");
                    for (int i = 0; i < conditions.size(); i++) {
                        if (i > 0) sql.append(" AND ");
                        sql.append(conditions.get(i).getColumn())
                                .append(" ")
                                .append(conditions.get(i).getOperator())
                                .append(" ?");
                    }
                }
            }
            default -> throw new UnsupportedOperationException(requestType + " doesn't support");
        }
        return sql.toString();
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                Config.getProperty("db_url"),
                Config.getProperty("db_username"),
                Config.getProperty("db_password")
        );

    }


    public static class DBRequestsBuilder {
        private RequestType requestType;
        private RequestTable requestTable;
        private List<Condition> condition = new ArrayList<>();

        public DBRequestsBuilder requestType(RequestType requestType) {
            this.requestType = requestType;
            return this;

        }

        public DBRequestsBuilder requestTable(RequestTable requestTable) {
            this.requestTable = requestTable;
            return this;
        }

        public DBRequestsBuilder where(Condition... condition) {
            this.condition.addAll(List.of(condition));
            return this;
        }

        public DBRequests buildRequest() {
            return DBRequests.builder()
                    .requestType(requestType)
                    .requestTable(requestTable)
                    .conditions(condition)
                    .build();
        }
    }
}
