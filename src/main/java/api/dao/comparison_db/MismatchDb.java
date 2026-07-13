package api.dao.comparison_db;

public class MismatchDb {
    public final String field;
    public final Object expected;
    public final Object actual;

    public MismatchDb(String field, Object expected, Object actual) {
        this.field = field;
        this.expected = expected;
        this.actual = actual;
    }
}
