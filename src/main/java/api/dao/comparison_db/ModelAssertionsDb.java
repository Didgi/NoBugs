package api.dao.comparison_db;

import org.assertj.core.api.AbstractAssert;

public class ModelAssertionsDb extends AbstractAssert<ModelAssertionsDb, Object> {

    private final Object request;
    private final Object response;

    private ModelAssertionsDb(Object request, Object response) {
        super(request, ModelAssertionsDb.class);
        this.request = request;
        this.response = response;
    }

    public static ModelAssertionsDb assertThatModels(Object req, Object res) {
        return new ModelAssertionsDb(req, res);
    }

    public ModelAssertionsDb match() {
        ModelComparisonConfigLoaderDb loader =
                new ModelComparisonConfigLoaderDb("model-comparison-db.properties");

        ComparisonRuleDb rule = loader.getRuleFor(request.getClass());

        if (rule == null) {
            failWithMessage("No config for %s", request.getClass().getSimpleName());
        }

        ComparisonResultDb result = ModelComparatorDb.compare(
                request,
                response,
                rule.getRules()
        );

        if (!result.isSuccess()) {
            failWithMessage(result.toString());
        }

        return this;
    }
}
