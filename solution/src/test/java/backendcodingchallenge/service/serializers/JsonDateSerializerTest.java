package backendcodingchallenge.service.serializers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JsonDateSerializerTest {

    private Writer jsonWriter;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private SimpleDateFormat sdf = new SimpleDateFormat(JsonDateSerializer.FORMAT);
    private JsonDateSerializer serializer;

    @Before
    public void setUp() throws IOException {
        jsonWriter = new StringWriter();
        jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        serializerProvider = new ObjectMapper().getSerializerProvider();
        serializer = new JsonDateSerializer();
    }

    @Test
    public void serializeDateOK() throws IOException, ParseException {
        serializer.serialize(sdf.parse("04/5/1993"), jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertThat(jsonWriter.toString(), is(equalTo("\"04/05/1993\"")));
    }

    @Test(expected = NullPointerException.class)
    public void serializeNullDateKO_NPE() throws IOException {
        new JsonAmountSerializer().serialize(null, jsonGenerator, serializerProvider);
    }

}
