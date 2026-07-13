package api.dao.comparison_db;

public class RuleDb {
    private final FieldRuleDb rule;
    private final String param;

    public RuleDb(FieldRuleDb rule, String param) {
        this.rule = rule;
        this.param = param;
    }

    public FieldRuleDb getRule() {
        return rule;
    }

    public String getParam() {
        return param;
    }
}
