package api.dao.comparison_db;

import java.util.List;

public class ComparisonResultDb {

    private final List<MismatchDb> mismatches;

    public ComparisonResultDb(List<MismatchDb> mismatches) {
        this.mismatches = mismatches;
    }

    public boolean isSuccess() {
        return mismatches.isEmpty();
    }

    public List<MismatchDb> getMismatches() {
        return mismatches;
    }

    @Override
    public String toString() {
        if (isSuccess()) return "All fields match";

        StringBuilder sb = new StringBuilder("Mismatches:\n");
        for (MismatchDb m : mismatches) {
            sb.append("- ")
                    .append(m.field)
                    .append(": expected=")
                    .append(m.expected)
                    .append(", actual=")
                    .append(m.actual)
                    .append("\n");
        }
        return sb.toString();
    }
}
