package backendcodingchallenge.service.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class JsonDateSerializer extends JsonSerializer<Date> {

    public static final String FORMAT = "dd/MM/yyyy";

    @Override
    public void serialize (Date value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT);
        format.setLenient(false);
        gen.writeString(format.format(value));
    }
}