package backendcodingchallenge.service.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class JsonAmountDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser jsonparser,
        DeserializationContext deserializationcontext) throws IOException {
        //Here, we don't call jsonparser.getValueAsDouble() directly, because it is too lenient and would convert "foo" into 0
        return (int)(Math.round(Double.parseDouble(jsonparser.getValueAsString())*100));
    }
}