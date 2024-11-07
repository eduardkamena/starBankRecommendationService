package pro.sky.recommendation_service.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sky.recommendation_service.exception.DoesNotEnumException;

import java.io.IOException;
import java.util.Arrays;

public class RulesQueryENUMDeserializer extends JsonDeserializer<RulesQueryENUM> {

    private final Logger logger = LoggerFactory.getLogger(RulesQueryENUMDeserializer.class);

    @Override
    public RulesQueryENUM deserialize(JsonParser p, DeserializationContext src) throws IOException {
        String value = p.getText().toUpperCase();
        for (RulesQueryENUM enumValue : RulesQueryENUM.values()) {
            if (enumValue.name().equals(value)) {
                return enumValue;
            }
        }
        logger.error("Error query must be valid ENUM value");
        throw new DoesNotEnumException("Query \"" + value + "\" should be one of the values in RulesQueryENUM: " + Arrays.toString(RulesQueryENUM.values()));
    }

}
