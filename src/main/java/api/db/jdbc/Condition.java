package api.db.jdbc;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Condition {
    private String column;
    private Object value;
    private String operator;

    public static Condition equalTo(String column, Object value){
        return new Condition(column, value, "=");
    }

    public static Condition notEqualTo(String column, Object value){
        return new Condition(column, value, "!=");
    }

    public static Condition like(String column, Object value){
        return new Condition(column, value, "like");
    }

}
