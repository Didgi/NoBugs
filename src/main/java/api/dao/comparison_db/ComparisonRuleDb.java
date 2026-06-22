package api.dao.comparison_db;

import java.util.Map;

public class ComparisonRuleDb {

    private final String requestClass;
    private final String responseClass;
    private final Map<String, RuleDb> rules;

    public ComparisonRuleDb(String requestClass, String responseClass, Map<String, RuleDb> rules) {
        this.requestClass = requestClass;
        this.responseClass = responseClass;
        this.rules = rules;
    }

    public Map<String, RuleDb> getRules() {
        return rules;
    }
}
