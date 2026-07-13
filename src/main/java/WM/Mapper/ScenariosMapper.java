package WM.Mapper;

import WM.models.FraudResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.annotations.FraudCheckMock;

public interface ScenariosMapper {
    FraudResponse map(FraudCheckMock config) throws RuntimeException;

    default String toJson(FraudCheckMock config) {
        try {
            return new ObjectMapper().writeValueAsString(map(config));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
