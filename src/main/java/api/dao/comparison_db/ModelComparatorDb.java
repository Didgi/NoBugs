package api.dao.comparison_db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ModelComparatorDb {

    public static ComparisonResultDb compare(
            Object request,
            Object response,
            Map<String, RuleDb> rules
    ) {

        List<MismatchDb> mismatches = new ArrayList<>();

        for (Map.Entry<String, RuleDb> entry : rules.entrySet()) {
            String field = entry.getKey();
            RuleDb rule = entry.getValue();

            MismatchDb mismatch = rule.getRule()
                    .validate(request, response, field, rule.getParam());

            if (mismatch != null) {
                mismatches.add(mismatch);
            }
        }

        return new ComparisonResultDb(mismatches);
    }
}
