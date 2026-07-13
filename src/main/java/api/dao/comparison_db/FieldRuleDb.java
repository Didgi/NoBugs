package api.dao.comparison_db;

import java.util.Objects;

public enum FieldRuleDb {

    //сравнивает значения одинаковых полей по имени в запросе/ответе
    STANDARD_EQUALS((req, res, field, param) -> {
        Object r1 = ReflectionUtilsDb.getFieldValue(req, field);
        Object r2 = ReflectionUtilsDb.getFieldValue(res, field);
        return Objects.equals(r1, r2)
                ? null
                : new MismatchDb(field, r1, r2);
    }),

    //проверяет, что поле должно существовать и не быть null
    REQUIRED((req, res, field, param) -> {
        Object value = ReflectionUtilsDb.getFieldValue(res, field);
        return value != null
                ? null
                : new MismatchDb(field, "not null", null);
    }),

    //проверяет, что поле должно существовать и быть null
    CHECK_NULL((req, res, field, param) -> {
        Object value = ReflectionUtilsDb.getFieldValue(res, field);
        return value == null
                ? null
                : new MismatchDb(field, null, value);
    }),

    //проверяет, что массив/список должен существовать и быть пустым
    ARRAY_EMPTY((req, res, field, param) -> {
        Object value = ReflectionUtilsDb.getFieldValue(res, field);
        return ReflectionUtilsDb.isEmpty(value)
                ? null
                : new MismatchDb(field, "empty", value);
    }),

    //проверяет, что массив/список должен существовать и не быть пустым
    ARRAY_NOT_EMPTY((req, res, field, param) -> {
        Object value = ReflectionUtilsDb.getFieldValue(res, field);
        return ReflectionUtilsDb.isNotEmpty(value)
                ? null
                : new MismatchDb(field, "not empty", value);
    }),

    //проверяет, что поле должно существовать и иметь значение true
    CHECK_TRUE((req, res, field, param) -> {
        Object value = ReflectionUtilsDb.getFieldValue(res, field);
        return Boolean.TRUE.equals(value)
                ? null
                : new MismatchDb(field, true, value);
    }),

    //проверяет, что поле должно существовать и иметь значение false
    CHECK_FALSE((req, res, field, param) -> {
        Object value = ReflectionUtilsDb.getFieldValue(res, field);
        return Boolean.FALSE.equals(value)
                ? null
                : new MismatchDb(field, false, value);
    }),

    //кастомное поле. Проверяет, что поле должно существовать и иметь значение больше указанного
    GREATER_THAN((req, res, field, param) -> {
        Object value = ReflectionUtilsDb.getFieldValue(res, field);
        if (value instanceof Number) {
            double actual = ((Number) value).doubleValue();
            double expected = Double.parseDouble(param);
            return actual > expected
                    ? null
                    : new MismatchDb(field, ">" + expected, actual);
        }
        return new MismatchDb(field, "number", value);
    }),

    //кастомное поле. Проверяет, что поле должно существовать и иметь значение меньше указанного
    LESS_THAN((req, res, field, param) -> {
        Object value = ReflectionUtilsDb.getFieldValue(res, field);
        if (value instanceof Number) {
            double actual = ((Number) value).doubleValue();
            double expected = Double.parseDouble(param);
            return actual < expected
                    ? null
                    : new MismatchDb(field, "<" + expected, actual);
        }
        return new MismatchDb(field, "number", value);
    }),

    //сравнивает значение поля в ответе со значением указанным в конфиге
    EQUALS((req, res, field, param) -> {
        Object value = ReflectionUtilsDb.getFieldValue(res, field);
        return Objects.equals(String.valueOf(value), param)
                ? null
                : new MismatchDb(field, param, value);
    });

    private final FieldCheck check;

    FieldRuleDb(FieldCheck check) {
        this.check = check;
    }

    public MismatchDb validate(Object req, Object res, String field, String param) {
        return check.apply(req, res, field, param);
    }

    @FunctionalInterface
    interface FieldCheck {
        MismatchDb apply(Object req, Object res, String field, String param);
    }
}
