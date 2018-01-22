package backendcodingchallenge;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class JsonDateSerializer extends JsonSerializer<LocalDate> {
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    @Override
    public void serialize(LocalDate date, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        gen.writeString(date.format(dateFormat));
    }
}
