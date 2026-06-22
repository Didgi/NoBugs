package api.dao.comparison_db;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ModelComparisonConfigLoaderDb {

    private final Properties props = new Properties();

    public ModelComparisonConfigLoaderDb(String file) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(file)) {
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ComparisonRuleDb getRuleFor(Class<?> clazz) {
        String name = clazz.getSimpleName();
        String value = props.getProperty(name);

        if (value == null) return null;

        String[] parts = value.split(":");
        String responseClass = parts[0];
        String rulesPart = parts[1];

        return new ComparisonRuleDb(name, responseClass, parseRules(rulesPart));
    }

    private Map<String, RuleDb> parseRules(String rulesPart) {
        Map<String, RuleDb> map = new HashMap<>();

        String[] entries = rulesPart.split(",");

        for (String entry : entries) {
            String[] kv = entry.split("=");

            String field = kv[0].trim();
            String ruleRaw = kv[1].trim();

            String[] ruleParts = ruleRaw.split(":");

            FieldRuleDb rule = FieldRuleDb.valueOf(ruleParts[0].toUpperCase());
            String param = ruleParts.length > 1 ? ruleParts[1] : null;

            map.put(field, new RuleDb(rule, param));
        }

        return map;
    }
}
